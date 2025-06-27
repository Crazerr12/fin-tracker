package ru.crazerr.feature.transactions.presentation.transactionsStory

import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

data class TransactionsStoryArgs(
    val categoryIds: LongArray?,
    val accountIds: LongArray?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val transactionType: TransactionType,
    val fromAnalysis: Boolean = false,
)
