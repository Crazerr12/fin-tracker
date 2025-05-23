package ru.crazerr.feature.budget.presentation.budgetEditor

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.presentation.formatWithAmountZeros
import ru.crazerr.core.utils.presentation.isValidAmount
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.budget.presentation.R
import ru.crazerr.feature.domain.api.Budget
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.core.utils.R as utilsR

class BudgetEditorComponent(
    componentContext: ComponentContext,
    private val onAction: (BudgetEditorComponentAction) -> Unit,
    private val dependencies: BudgetEditorDependencies,
) : BaseComponent<BudgetEditorState, BudgetEditorViewAction>(InitialBudgetEditorState),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        handleInputFlow(flow = dependencies.args.inputFlow)
        reduceState { copy(id = dependencies.args.id) }
        getInitialData()
    }

    override fun handleViewAction(action: BudgetEditorViewAction) {
        when (action) {
            BudgetEditorViewAction.BackClick -> onAction(BudgetEditorComponentAction.BackClick)
            BudgetEditorViewAction.CheckAlarmBudgetExceeded -> onCheckAlarmBudgetExceeded()
            BudgetEditorViewAction.CheckIsRegular -> onCheckIsRegular()
            BudgetEditorViewAction.CheckWarningBudgetClose -> onCheckWarningBudgetClose()
            BudgetEditorViewAction.ManageCategoriesDropdown -> onManageCategoriesDropdown()
            BudgetEditorViewAction.SaveClick -> onSaveClick()
            is BudgetEditorViewAction.UpdateMaxAmount -> onUpdateMaxAmount(maxAmount = action.maxAmount)
            is BudgetEditorViewAction.UpdateSelectedCategory -> onUpdateSelectedCategory(category = action.category)
        }
    }

    private fun onSaveClick() {
        handleUserInput {
            coroutineScope.launch {
                reduceState { copy(buttonIsLoading = true) }
                val budget = Budget(
                    id = state.value.id,
                    category = state.value.selectedCategory,
                    maxAmount = state.value.maxAmount.toDouble(),
                    currentAmount = state.value.currentAmount,
                    repeatBudgetId = state.value.repeatBudgetId,
                    date = state.value.date,
                    isRegular = state.value.isRegular,
                    isWarning = state.value.isWarning,
                    isAlarm = state.value.isAlarm,
                )

                val saveResult = when {
                    state.value.id != -1L -> dependencies.budgetRepository.updateBudget(budget)
                    state.value.repeatBudgetId == null && state.value.isRegular -> {
                        dependencies.budgetRepository.createRepeatBudget(budget)
                    }

                    else -> dependencies.budgetRepository.createBudget(budget)
                }

                saveResult.fold(
                    onSuccess = {
                        reduceState { copy(buttonIsLoading = false) }
                        onAction(BudgetEditorComponentAction.SaveClick(budget = it))
                    },
                    onFailure = {
                        reduceState { copy(buttonIsLoading = false) }
                        snackbarManager.showSnackbar(message = it.localizedMessage ?: "")
                    },
                )
            }
        }
    }

    private fun onManageCategoriesDropdown() {
        reduceState { copy(categoriesDropdownIsExpand = !categoriesDropdownIsExpand) }
    }

    private fun onCheckAlarmBudgetExceeded() {
        reduceState { copy(isAlarm = !isAlarm) }
    }

    private fun onCheckIsRegular() {
        reduceState { copy(isRegular = !isRegular) }
    }

    private fun onCheckWarningBudgetClose() {
        reduceState { copy(isWarning = !isWarning) }
    }

    private fun onUpdateMaxAmount(maxAmount: String) {
        maxAmount.isValidAmount().fold(
            onSuccess = { reduceState { copy(maxAmount = it, maxAmountError = "") } },
            onFailure = {
                reduceState {
                    copy(
                        maxAmountError = dependencies.resourceManager.getString(
                            R.string.budget_editor_only_digits_field
                        )
                    )
                }
            }
        )
    }

    private fun onUpdateSelectedCategory(category: Category) {
        reduceState {
            copy(
                selectedCategory = category,
                categoriesDropdownIsExpand = false,
                selectedCategoryError = ""
            )
        }
        getCurrentAmount()
    }

    private fun getInitialData() {
        coroutineScope.launch {
            val categoriesResult = async { dependencies.categoryRepository.getAllCategories() }

            if (state.value.id != -1L) {
                val budgetResult = dependencies.budgetRepository.getBudgetById(id = state.value.id)

                budgetResult.fold(
                    onSuccess = {
                        reduceState {
                            copy(
                                id = it.id,
                                repeatBudgetId = it.repeatBudgetId,
                                selectedCategory = it.category,
                                isRegular = it.isRegular,
                                isAlarm = it.isAlarm,
                                isWarning = it.isWarning,
                                date = it.date,
                                currentAmount = it.currentAmount,
                                maxAmount = it.maxAmount.formatWithAmountZeros(),
                            )
                        }
                    },
                    onFailure = {
                        snackbarManager.showSnackbar(
                            message = it.localizedMessage ?: ""
                        )
                    },
                )
            }

            categoriesResult.await().fold(
                onSuccess = {
                    reduceState {
                        copy(
                            categories = it,
                            selectedCategory = if (selectedCategory.id != -1L) selectedCategory else it[0]
                        )
                    }

                    getCurrentAmount()
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") }
            )
        }
    }

    private fun getCurrentAmount() {
        coroutineScope.launch {
            dependencies.transactionRepository.getCurrentAmount(
                categoryId = state.value.selectedCategory.id,
                date = state.value.date,
            )
                .fold(onSuccess = {
                    reduceState { copy(currentAmount = it) }
                }, onFailure = {
                    snackbarManager.showSnackbar(message = it.localizedMessage ?: "")
                    reduceState { copy(currentAmount = 0.0) }
                })
        }
    }

    private fun handleInputFlow(flow: SharedFlow<Input>) {
        coroutineScope.launch {
            flow.collectLatest { input ->
                when (input) {
                    is Input.AddCategory -> handleAddCategoryInput(category = input.category)
                }
            }
        }
    }

    private fun handleAddCategoryInput(category: Category) {
        reduceState { copy(categories = categories + category, selectedCategory = category) }
    }

    private fun handleUserInput(block: () -> Unit) {
        var isValid = true

        if (state.value.selectedCategory.id == -1L) {
            reduceState { copy(selectedCategoryError = dependencies.resourceManager.getString(utilsR.string.empty_field_error)) }
            isValid = false
        }

        if (state.value.maxAmount.isBlank()) {
            reduceState { copy(maxAmountError = dependencies.resourceManager.getString(utilsR.string.empty_field_error)) }
            isValid = false
        }

        if (isValid) block()
    }

    sealed interface Input {
        data class AddCategory(val category: Category) : Input
    }
}