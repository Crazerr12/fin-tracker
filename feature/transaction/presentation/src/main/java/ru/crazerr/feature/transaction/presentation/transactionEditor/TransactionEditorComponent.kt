package ru.crazerr.feature.transaction.presentation.transactionEditor

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.presentation.formatWithAmountZeros
import ru.crazerr.core.utils.presentation.isValidAmount
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transaction.presentation.R
import java.time.LocalDate
import kotlin.math.abs
import ru.crazerr.core.utils.R as utilsR

class TransactionEditorComponent(
    componentContext: ComponentContext,
    private val onAction: (TransactionEditorComponentAction) -> Unit,
    private val dependencies: TransactionEditorDependencies,
) : BaseComponent<TransactionEditorState, TransactionEditorViewAction>(InitialTransactionEditorState),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        if (dependencies.args.id != -1L) {
            getTransaction()
        }
        getAccountsAndCategories()
        handleInput(input = dependencies.args.input)
    }

    override fun handleViewAction(action: TransactionEditorViewAction) {
        when (action) {
            TransactionEditorViewAction.BackClick -> onAction(TransactionEditorComponentAction.BackClick)
            TransactionEditorViewAction.ManageAccountDropdown -> onManageAccountDropdown()
            TransactionEditorViewAction.ManageCategoryDropdown -> onManageCategoryDropdown()
            TransactionEditorViewAction.SaveClick -> onSaveClick()
            is TransactionEditorViewAction.SelectAccount -> onSelectAccount(account = action.account)
            is TransactionEditorViewAction.SelectCategory -> onSelectCategory(category = action.category)
            is TransactionEditorViewAction.SelectTransactionType -> onSelectTransactionType(
                transactionType = action.transactionType
            )

            is TransactionEditorViewAction.UpdateAmount -> onUpdateAmount(amount = action.amount)
            TransactionEditorViewAction.CreateNewAccount -> onAction(
                TransactionEditorComponentAction.CreateNewAccount
            )

            TransactionEditorViewAction.CreateNewCategory -> onAction(
                TransactionEditorComponentAction.CreateNewCategory
            )

            TransactionEditorViewAction.ManageDateDialog -> onManageDateDialog()
            is TransactionEditorViewAction.SaveDate -> onSaveDate(date = action.date)
        }
    }

    private fun onSaveDate(date: LocalDate) {
        reduceState { copy(date = date) }
    }

    private fun onManageDateDialog() {
        reduceState { copy(dateDialogIsExpanded = !dateDialogIsExpanded) }
    }

    private fun onManageAccountDropdown() {
        reduceState { copy(accountsDropdownIsExpanded = !accountsDropdownIsExpanded) }
    }

    private fun onUpdateAmount(amount: String) {
        amount.isValidAmount().fold(
            onSuccess = { reduceState { copy(amount = it, amountError = "") } },
            onFailure = { reduceState { copy(amountError = dependencies.resourceManager.getString(R.string.transaction_editor_amount_only_digits_error)) } }
        )
    }

    private fun onSelectAccount(account: Account) {
        reduceState {
            copy(
                selectedAccount = account,
                accountsDropdownIsExpanded = false,
                selectedAccountError = "",
            )
        }
    }

    private fun onSelectCategory(category: Category) {
        reduceState { copy(selectedCategory = category, categoriesDropdownIsExpanded = false) }
    }

    private fun onSelectTransactionType(transactionType: TransactionType) {
        reduceState { copy(transactionType = transactionType) }
    }

    private fun onManageCategoryDropdown() {
        reduceState { copy(categoriesDropdownIsExpanded = !categoriesDropdownIsExpanded) }
    }

    private fun onSaveClick() {
        validateUserInput {
            coroutineScope.launch {
                reduceState { copy(buttonIsLoading = true) }

                val result = if (state.value.id == -1L) {
                    dependencies.transactionRepository.createTransaction(
                        transaction = Transaction(
                            id = state.value.id,
                            category = state.value.selectedCategory,
                            amount = abs(state.value.amount.toDouble()),
                            type = state.value.transactionType,
                            date = state.value.date,
                            account = state.value.selectedAccount
                        )
                    )
                } else {
                    dependencies.transactionRepository.updateTransaction(
                        transaction = Transaction(
                            id = state.value.id,
                            category = state.value.selectedCategory,
                            amount = abs(state.value.amount.toDouble()),
                            type = state.value.transactionType,
                            date = state.value.date,
                            account = state.value.selectedAccount,
                        )
                    )
                }

                result.fold(
                    onSuccess = {
                        if (it.second.percentage > 1.0 && it.second.isAlarm) {
                            dependencies.notificationSender.sendNotification(
                                title = dependencies.resourceManager.getString(
                                    R.string.transaction_editor_budget_exceeded_title,
                                    it.first.category.name,
                                ),
                                textContent = dependencies.resourceManager.getString(R.string.transaction_editor_budget_exceeded_description)
                            )
                        } else if (it.second.percentage >= 0.7 && it.second.isWarning) {
                            dependencies.notificationSender.sendNotification(
                                title = dependencies.resourceManager.getString(
                                    R.string.transaction_editor_budget_is_almost_exhausted_title,
                                    it.first.category.name,
                                ),
                                textContent = dependencies.resourceManager.getString(R.string.transaction_editor_budget_is_almost_exhausted_description)
                            )
                        }

                        onAction(TransactionEditorComponentAction.SavedClick(it.first))
                    },
                    onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") }
                )

                reduceState { copy(buttonIsLoading = false) }
            }
        }
    }

    private fun getTransaction() {
        coroutineScope.launch {
            reduceState { copy(id = dependencies.args.id) }
            val result =
                dependencies.transactionRepository.getTransactionById(id = dependencies.args.id)

            result.fold(
                onSuccess = {
                    reduceState {
                        copy(
                            id = it.id,
                            transactionType = it.type,
                            selectedAccount = it.account,
                            amount = it.amount.formatWithAmountZeros(),
                            selectedCategory = it.category,
                            date = it.date,
                        )
                    }
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )
        }
    }

    private fun getAccountsAndCategories() {
        coroutineScope.launch {
            val accountsResult = async { dependencies.accountRepository.getAccounts() }
            val categoriesResult = async { dependencies.categoryRepository.getCategories() }

            accountsResult.await().first().fold(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        reduceState { copy(accounts = it, selectedAccount = it[0]) }
                    }
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") }
            )

            categoriesResult.await().fold(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        reduceState { copy(categories = it, selectedCategory = it[0]) }
                    }
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") }
            )
        }
    }

    private fun validateUserInput(block: () -> Unit) {
        var isValid = true

        if (state.value.amount.isBlank()) {
            reduceState { copy(amountError = dependencies.resourceManager.getString(R.string.transaction_editor_amount_empty_error)) }
            isValid = false
        }

        if (state.value.selectedAccount.id == -1L) {
            reduceState { copy(selectedAccountError = dependencies.resourceManager.getString(utilsR.string.empty_field_error)) }
            isValid = false
        }

        if (isValid) {
            block()
        }
    }

    private fun handleInput(input: MutableSharedFlow<Input>) {
        coroutineScope.launch {
            input.collectLatest {
                when (it) {
                    is Input.AccountInput -> reduceState {
                        copy(
                            selectedAccount = it.account,
                            accounts = accounts + it.account,
                            accountsDropdownIsExpanded = false,
                        )
                    }

                    is Input.CategoryInput -> reduceState {
                        copy(
                            selectedCategory = it.category,
                            categories = categories + it.category,
                            categoriesDropdownIsExpanded = false,
                        )
                    }
                }
            }
        }
    }

    sealed interface Input {
        data class CategoryInput(val category: Category) : Input
        data class AccountInput(val account: Account) : Input
    }
}