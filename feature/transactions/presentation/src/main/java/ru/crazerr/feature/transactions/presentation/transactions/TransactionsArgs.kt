package ru.crazerr.feature.transactions.presentation.transactions

import kotlinx.coroutines.flow.MutableSharedFlow
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

class TransactionsArgs(
    val inputFlow: MutableSharedFlow<TransactionsComponent.Input>,
    val categoryIds: LongArray?,
    val accountIds: LongArray?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val transactionType: TransactionType,
)