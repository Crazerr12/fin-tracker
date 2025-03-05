package ru.crazerr.feature.transaction.data.repository

import ru.crazerr.feature.transaction.data.dataSource.LocalTransactionDataSource
import ru.crazerr.feature.transaction.domain.TransactionRepository
import ru.crazerr.feature.transaction.domain.api.Transaction

internal class TransactionRepositoryImpl(
    private val localTransactionDataSource: LocalTransactionDataSource,
) : TransactionRepository {
    override suspend fun createTransaction(transaction: Transaction): Result<Transaction> {
        return localTransactionDataSource.createTransaction(transaction = transaction)
    }

    override suspend fun getTransactionById(id: Int): Result<Transaction> {
        return localTransactionDataSource.getTransactionById(id = id)
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Transaction> {
        return localTransactionDataSource.updateTransaction(transaction = transaction)
    }
}