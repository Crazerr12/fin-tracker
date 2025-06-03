package ru.crazerr.feature.transactions.presentation.transactionsFilter

import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transactions.presentation.transactionsFilter.model.FilterType
import java.time.LocalDate

data class TransactionsFilterState(
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedAccountIds: List<Long>,
    val selectedCategoryIds: List<Long>,
    val isFilterChanged: Boolean = false,
    val startDate: LocalDate? = null,
    val startDateDialogIsOpen: Boolean = false,
    val endDate: LocalDate? = null,
    val endDateDialogIsOpen: Boolean = false,
    val selectedFilterType: FilterType = FilterType.Category,
    val searchQuery: String = "",
    val isLoading: Boolean = true,
) {
    val resetButtonIsVisible: Boolean = selectedCategoryIds.size != categories.size
            || selectedAccountIds.size != accounts.size || startDate != null || endDate != null
}