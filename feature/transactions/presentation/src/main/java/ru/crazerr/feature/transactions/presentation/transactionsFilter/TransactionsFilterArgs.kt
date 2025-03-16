package ru.crazerr.feature.transactions.presentation.transactionsFilter

import java.time.LocalDate

class TransactionsFilterArgs(
    val accountIds: IntArray,
    val categoryIds: IntArray,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val isFilterEnabled: Boolean,
)