package ru.crazerr.feature.transactions.data.dataSource

import androidx.room.InvalidationTracker
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.account.data.api.toAccountEntity
import ru.crazerr.feature.transaction.data.api.toTransaction
import ru.crazerr.feature.transaction.data.api.toTransactionEntity
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

internal class LocalTransactionsDataSource(
    private val appDatabase: AppDatabase,
) {
    private val transactionsDao = appDatabase.transactionsDao()
    private val accountsDao = appDatabase.accountsDao()
    private val budgetsDao = appDatabase.budgetsDao()

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

    suspend fun deleteTransaction(transaction: Transaction): Result<Transaction> = runCatching {
        val amount = if (transaction.type == TransactionType.Income) -transaction.amount
        else transaction.amount

        val budget = budgetsDao.getBudgetByCategoryAndDate(
            categoryId = transaction.category.id,
            date = LocalDate.now().toString(),
        )

        appDatabase.withTransaction {
            transactionsDao.delete(transaction.toTransactionEntity())
            accountsDao.update(
                transaction.account.copy(amount = transaction.account.amount + amount)
                    .toAccountEntity()
            )

            if (budget != null) {
                budgetsDao.update(budget.copy(currentAmount = budget.currentAmount + amount))
            }

            transaction
        }
    }

    fun addObserver(observer: InvalidationTracker.Observer) {
        invalidationTracker.addObserver(observer = observer)
    }

    fun removeObserver(observer: InvalidationTracker.Observer) {
        invalidationTracker.removeObserver(observer = observer)
    }
}