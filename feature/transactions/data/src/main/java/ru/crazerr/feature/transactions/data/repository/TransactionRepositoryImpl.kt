package ru.crazerr.feature.transactions.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.InvalidationTracker
import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transactions.data.dataSource.LocalTransactionsDataSource
import ru.crazerr.feature.transactions.data.pagingSource.TransactionsPagingSource
import ru.crazerr.feature.transactions.domain.repository.TransactionRepository
import java.time.LocalDate

internal class TransactionRepositoryImpl(
    private val localTransactionsDataSource: LocalTransactionsDataSource,
) : TransactionRepository {

    init {
        localTransactionsDataSource.addObserver(observer = object :
            InvalidationTracker.Observer("transactions") {
            override fun onInvalidated(tables: Set<String>) {
            }
        })
    }

    override fun getTransactions(
        accountIds: LongArray,
        categoryIds: LongArray,
        transactionType: TransactionType,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Flow<PagingData<Pair<LocalDate, List<Transaction>>>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                TransactionsPagingSource(
                    categoryIds = categoryIds,
                    accountIds = accountIds,
                    transactionType = transactionType,
                    startDate = startDate,
                    endDate = endDate,
                    localTransactionsDataSource = localTransactionsDataSource,
                )
            }
        ).flow
    }

    override suspend fun deleteTransaction(transaction: Transaction): Result<Transaction> =
        localTransactionsDataSource.deleteTransaction(transaction)
}