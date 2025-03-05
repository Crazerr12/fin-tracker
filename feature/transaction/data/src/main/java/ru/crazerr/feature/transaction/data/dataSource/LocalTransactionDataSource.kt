package ru.crazerr.feature.transaction.data.dataSource

import ru.crazerr.core.database.transactions.dao.TransactionsDao
import ru.crazerr.feature.transaction.data.model.toTransaction
import ru.crazerr.feature.transaction.data.model.toTransactionEntity
import ru.crazerr.feature.transaction.domain.api.Transaction

internal class LocalTransactionDataSource(
    private val transactionsDao: TransactionsDao,
) {
    suspend fun getTransactionById(id: Int): Result<Transaction> = try {
        val transactionEntity = transactionsDao.getTransactionById(id = id)

        Result.success(transactionEntity.toTransaction())
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    suspend fun createTransaction(transaction: Transaction): Result<Transaction> = try {
        val id = transactionsDao.insert(transaction.toTransactionEntity())[0]

        Result.success(transaction.copy(id = id.toInt()))
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    suspend fun updateTransaction(transaction: Transaction): Result<Transaction> = try {
        transactionsDao.update(transaction.toTransactionEntity())

        Result.success(transaction)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}