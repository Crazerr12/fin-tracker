package ru.crazerr.feature.transactions.presentation.transactionsFilter

import java.time.LocalDate

class TransactionsFilterArgs(
    val accountIds: LongArray,
    val categoryIds: LongArray,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val isFilterEnabled: Boolean,
)