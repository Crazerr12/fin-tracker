package ru.crazerr.feature.transactions.data.dataSource

import androidx.room.InvalidationTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.transaction.data.api.toTransaction
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

internal class LocalTransactionsDataSource(
    appDatabase: AppDatabase,
) {
    private val transactionsDao = appDatabase.transactionsDao()

    private val invalidationTracker = appDatabase.invalidationTracker

    fun getTransactionsByFilter(
        categoryIds: LongArray,
        accountIds: LongArray,
        dates: List<String>,
        transactionType: TransactionType,
    ): Result<Flow<List<Transaction>>> = try {
        val transactionFlow = transactionsDao.getTransactionsByFilters(
            accountIds = accountIds,
            categoryIds = categoryIds,
            transactionType = transactionType.id,
            dates = dates
        )

        Result.success(transactionFlow.map { it.map { it.toTransaction() } })
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    suspend fun getPagingDates(
        limit: Int,
        offset: Int,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Result<List<LocalDate>> = try {
        val dates = transactionsDao.getPagingDates(
            limit = limit,
            offset = offset,
            startDate = startDate?.toString(),
            endDate = endDate?.toString(),
        )

        Result.success(dates)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    fun addObserver(observer: InvalidationTracker.Observer) {
        invalidationTracker.addObserver(observer = observer)
    }

    fun removeObserver(observer: InvalidationTracker.Observer) {
        invalidationTracker.removeObserver(observer = observer)
    }
}