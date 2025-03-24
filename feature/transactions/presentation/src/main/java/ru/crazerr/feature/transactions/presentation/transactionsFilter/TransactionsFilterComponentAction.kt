package ru.crazerr.feature.transactions.presentation.transactionsFilter

import java.time.LocalDate

sealed interface TransactionsFilterComponentAction {
    data object BackClick : TransactionsFilterComponentAction
    data class SaveButtonClick(
        val accountIds: LongArray,
        val categoryIds: LongArray,
        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val isFilterEnabled: Boolean
    ) : TransactionsFilterComponentAction
}