package ru.crazerr.feature.transactions.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

interface TransactionRepository {
    fun getTransactions(
        accountIds: IntArray,
        categoryIds: IntArray,
        transactionType: TransactionType,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Flow<PagingData<Pair<LocalDate, List<Transaction>>>>
}