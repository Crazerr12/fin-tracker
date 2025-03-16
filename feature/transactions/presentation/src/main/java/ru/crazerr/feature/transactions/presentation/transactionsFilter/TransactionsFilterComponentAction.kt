package ru.crazerr.feature.transactions.presentation.transactionsFilter

import java.time.LocalDate

sealed interface TransactionsFilterComponentAction {
    data object BackClick : TransactionsFilterComponentAction
    data class SaveButtonClick(
        val accountIds: IntArray,
        val categoryIds: IntArray,
        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val isFilterEnabled: Boolean
    ) : TransactionsFilterComponentAction
}