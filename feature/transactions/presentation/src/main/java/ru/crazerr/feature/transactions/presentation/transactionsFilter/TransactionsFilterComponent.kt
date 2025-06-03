package ru.crazerr.feature.transactions.presentation.transactionsFilter

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponentAction.*
import ru.crazerr.feature.transactions.presentation.transactionsFilter.model.FilterType
import java.time.LocalDate

class TransactionsFilterComponent(
    componentContext: ComponentContext,
    private val onAction: (TransactionsFilterComponentAction) -> Unit,
    private val dependencies: TransactionsFilterDependencies,
) : BaseComponent<TransactionsFilterState, TransactionsFilterViewAction>(
    TransactionsFilterState(
        selectedAccountIds = dependencies.args.accountIds.toList(),
        selectedCategoryIds = dependencies.args.categoryIds.toList(),
        startDate = dependencies.args.startDate,
        endDate = dependencies.args.endDate,
    )
), ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        getInitialData()
    }

    override fun handleViewAction(action: TransactionsFilterViewAction) {
        when (action) {
            is TransactionsFilterViewAction.ManageAccount -> onManageAccount(id = action.id)
            TransactionsFilterViewAction.ManageAllAccounts -> onManageAllAccounts()
            TransactionsFilterViewAction.ManageAllCategories -> onManageAllCategories()
            is TransactionsFilterViewAction.ManageCategory -> onManageCategory(id = action.id)
            is TransactionsFilterViewAction.SelectFilterType -> onSelectFilterType(filterType = action.filterType)
            TransactionsFilterViewAction.ResetAllFilters -> onResetAllFilters()
            TransactionsFilterViewAction.SaveButtonClick -> onAction(
                SaveButtonClick(
                    accountIds = state.value.selectedAccountIds.toLongArray(),
                    categoryIds = state.value.selectedCategoryIds.toLongArray(),
                    startDate = state.value.startDate,
                    endDate = state.value.endDate,
                    isFilterEnabled = state.value.isFilterChanged,
                )
            )

            TransactionsFilterViewAction.BackClick -> onAction(BackClick)
            is TransactionsFilterViewAction.UpdateSearch -> onUpdateSearch(searchQuery = action.searchQuery)
            TransactionsFilterViewAction.ManageEndDateDialog -> onManageEndDateDialog()
            TransactionsFilterViewAction.ManageStartDateDialog -> onManageStartDateDialog()
            is TransactionsFilterViewAction.UpdateEndDate -> onUpdateEndDate(endDate = action.endDate)
            is TransactionsFilterViewAction.UpdateStartDate -> onUpdateStartDate(startDate = action.startDate)
        }
    }

    private fun onUpdateEndDate(endDate: LocalDate?) {
        reduceState { copy(endDate = endDate) }
        checkForFilterChange()
    }

    private fun onUpdateStartDate(startDate: LocalDate?) {
        reduceState { copy(startDate = startDate) }
        checkForFilterChange()
    }

    private fun onManageStartDateDialog() {
        reduceState { copy(startDateDialogIsOpen = !startDateDialogIsOpen) }
    }

    private fun onManageEndDateDialog() {
        reduceState { copy(endDateDialogIsOpen = !endDateDialogIsOpen) }
    }

    private fun onUpdateSearch(searchQuery: String) {
        reduceState { copy(searchQuery = searchQuery) }
    }

    private fun onResetAllFilters() {
        reduceState {
            copy(
                selectedAccountIds = accounts.map { it.id },
                selectedCategoryIds = categories.map { it.id },
                startDate = null,
                endDate = null,
            )
        }
        checkForFilterChange()
    }

    private fun onSelectFilterType(filterType: FilterType) {
        reduceState { copy(selectedFilterType = filterType, searchQuery = "") }
    }

    private fun onManageCategory(id: Long) {
        reduceState {
            copy(
                selectedCategoryIds = if (selectedCategoryIds.contains(id)) {
                    selectedCategoryIds - id
                } else {
                    selectedCategoryIds + id
                }
            )
        }
        checkForFilterChange()
    }

    private fun onManageAccount(id: Long) {
        reduceState {
            copy(
                selectedAccountIds = if (selectedAccountIds.contains(id)) {
                    selectedAccountIds - id
                } else {
                    selectedAccountIds + id
                }
            )
        }
        checkForFilterChange()
    }

    private fun onManageAllCategories() {
        reduceState {
            copy(
                selectedCategoryIds = if (selectedCategoryIds.size == categories.size) {
                    emptyList()
                } else {
                    categories.map { it.id }
                }
            )
        }
        checkForFilterChange()
    }

    private fun onManageAllAccounts() {
        reduceState {
            copy(
                selectedAccountIds = if (selectedAccountIds.size == accounts.size) {
                    emptyList()
                } else {
                    accounts.map { it.id }
                }
            )
        }
        checkForFilterChange()
    }

    private fun checkForFilterChange() {
        reduceState {
            copy(
                isFilterChanged = selectedCategoryIds.size != dependencies.args.categoryIds.size || endDate != null
                        || selectedAccountIds.size != dependencies.args.accountIds.size || startDate != null
            )
        }
    }

    private fun getInitialData() {
        coroutineScope.launch {
            reduceState { copy(isLoading = true) }
            val categoryFlow = async { dependencies.categoryRepository.getCategories() }
            val accountFlow = async { dependencies.accountRepository.getAccounts() }

            categoryFlow.await().fold(
                onSuccess = {
                    val categories = it.first()
                    reduceState { copy(categories = categories) }
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )

            accountFlow.await().fold(
                onSuccess = {
                    val accounts = it.first()
                    reduceState { copy(accounts = accounts) }
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )

            checkForFilterChange()

            reduceState { copy(isLoading = false) }
        }
    }
}
