package ru.crazerr.feature.transactions.presentation.transactionsFilter

import ru.crazerr.feature.transactions.presentation.transactionsFilter.model.FilterType
import java.time.LocalDate

sealed interface TransactionsFilterViewAction {
    data object BackClick : TransactionsFilterViewAction

    data class UpdateSearch(val searchQuery: String) : TransactionsFilterViewAction

    data object ResetAllFilters : TransactionsFilterViewAction
    data class SelectFilterType(val filterType: FilterType) : TransactionsFilterViewAction

    data class ManageCategory(val id: Long) : TransactionsFilterViewAction
    data object ManageAllCategories : TransactionsFilterViewAction

    data class ManageAccount(val id: Long) : TransactionsFilterViewAction
    data object ManageAllAccounts : TransactionsFilterViewAction

    data object ManageStartDateDialog : TransactionsFilterViewAction
    data class UpdateStartDate(val startDate: LocalDate?) : TransactionsFilterViewAction

    data object ManageEndDateDialog : TransactionsFilterViewAction
    data class UpdateEndDate(val endDate: LocalDate?) : TransactionsFilterViewAction

    data object SaveButtonClick : TransactionsFilterViewAction
}