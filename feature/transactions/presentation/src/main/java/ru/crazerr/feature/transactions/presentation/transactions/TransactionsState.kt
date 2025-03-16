package ru.crazerr.feature.transactions.presentation.transactions

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

data class TransactionsState(
    val transactions: Flow<PagingData<Pair<LocalDate, List<Transaction>>>>,
    val categoryIds: IntArray,
    val accountIds: IntArray,
    val selectedTransactionType: TransactionType,
    val isFilterEnabled: Boolean,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
)

internal val InitialTransactionsState = TransactionsState(
    transactions = emptyFlow(),
    categoryIds = intArrayOf(),
    accountIds = intArrayOf(),
    selectedTransactionType = TransactionType.All,
    isFilterEnabled = false,
    startDate = null,
    endDate = null,
)