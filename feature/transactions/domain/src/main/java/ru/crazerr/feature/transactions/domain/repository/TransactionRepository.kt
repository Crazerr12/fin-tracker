package ru.crazerr.feature.transactions.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

interface TransactionRepository {
    fun getTransactions(
        accountIds: LongArray,
        categoryIds: LongArray,
        transactionType: TransactionType,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Flow<PagingData<Pair<LocalDate, List<Transaction>>>>

    suspend fun deleteTransaction(transaction: Transaction): Result<Transaction>
}