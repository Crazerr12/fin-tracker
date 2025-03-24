package ru.crazerr.feature.transactions.presentation.transactionsFilter

import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transactions.presentation.transactionsFilter.model.FilterType
import java.time.LocalDate

data class TransactionsFilterState(
    val accounts: List<Account>,
    val categories: List<Category>,
    val selectedAccountIds: List<Long>,
    val selectedCategoryIds: List<Long>,
    val isFilterEnabled: Boolean,
    val startDate: LocalDate?,
    val startDateDialogIsOpen: Boolean,
    val endDate: LocalDate?,
    val endDateDialogIsOpen: Boolean,
    val selectedFilterType: FilterType,
    val searchQuery: String,
)

internal val InitialTransactionsFilterState = TransactionsFilterState(
    accounts = emptyList(),
    categories = emptyList(),
    selectedAccountIds = emptyList(),
    selectedCategoryIds = emptyList(),
    isFilterEnabled = false,
    startDate = null,
    startDateDialogIsOpen = false,
    endDate = null,
    endDateDialogIsOpen = false,
    selectedFilterType = FilterType.Category,
    searchQuery = "",
)