package ru.crazerr.feature.transactions.presentation.transactionsFilter

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
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
    InitialTransactionsFilterState
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
                    accountIds = state.value.selectedAccountIds.toIntArray(),
                    categoryIds = state.value.selectedCategoryIds.toIntArray(),
                    startDate = state.value.startDate,
                    endDate = state.value.endDate,
                    isFilterEnabled = state.value.isFilterEnabled,
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
        checkForFilterEnable()
    }

    private fun onUpdateStartDate(startDate: LocalDate?) {
        reduceState { copy(startDate = startDate) }
        checkForFilterEnable()
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
        checkForFilterEnable()
    }

    private fun onSelectFilterType(filterType: FilterType) {
        reduceState { copy(selectedFilterType = filterType, searchQuery = "") }
    }

    private fun onManageCategory(id: Int) {
        reduceState {
            copy(
                selectedCategoryIds = if (selectedCategoryIds.contains(id)) {
                    selectedCategoryIds - id
                } else {
                    selectedCategoryIds + id
                }
            )
        }
        checkForFilterEnable()
    }

    private fun onManageAccount(id: Int) {
        reduceState {
            copy(
                selectedAccountIds = if (selectedAccountIds.contains(id)) {
                    selectedAccountIds - id
                } else {
                    selectedAccountIds + id
                }
            )
        }
        checkForFilterEnable()
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
        checkForFilterEnable()
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
        checkForFilterEnable()
    }

    private fun checkForFilterEnable() {
        reduceState {
            copy(
                isFilterEnabled = selectedCategoryIds.size != dependencies.args.categoryIds.size || endDate != null
                        || selectedAccountIds.size != dependencies.args.accountIds.size || startDate != null
            )
        }
    }

    private fun getInitialData() {
        coroutineScope.launch {
            val categories = async { dependencies.categoryRepository.getCategories() }
            val accounts = async { dependencies.accountRepository.getAccounts() }

            categories.await().fold(
                onSuccess = { reduceState { copy(categories = it) } },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )

            accounts.await().fold(
                onSuccess = { reduceState { copy(accounts = it) } },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )

            handleArgs()
        }
    }

    private fun handleArgs() {
        reduceState {
            copy(
                selectedAccountIds = dependencies.args.accountIds.toList(),
                selectedCategoryIds = dependencies.args.categoryIds.toList(),
                startDate = dependencies.args.startDate,
                endDate = dependencies.args.endDate,
            )
        }
        checkForFilterEnable()
    }
}
