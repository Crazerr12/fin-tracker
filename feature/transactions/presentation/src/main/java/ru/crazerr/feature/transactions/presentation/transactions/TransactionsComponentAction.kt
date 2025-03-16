package ru.crazerr.feature.transactions.presentation.transactions

import java.time.LocalDate

sealed interface TransactionsComponentAction {
    data class GoToFilter(
        val accountIds: IntArray,
        val categoryIds: IntArray,
        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val isFilterEnabled: Boolean
    ) : TransactionsComponentAction

    data class OpenTransactionEditor(val id: Int) : TransactionsComponentAction
}