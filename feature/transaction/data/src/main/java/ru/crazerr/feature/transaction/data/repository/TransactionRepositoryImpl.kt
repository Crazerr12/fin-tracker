package ru.crazerr.feature.transaction.data.repository

import ru.crazerr.feature.transaction.data.dataSource.TransactionLocalDataSource
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.model.BudgetNotification
import ru.crazerr.feature.transaction.domain.repository.TransactionRepository

internal class TransactionRepositoryImpl(
    private val transactionLocalDataSource: TransactionLocalDataSource,
) : TransactionRepository {
    override suspend fun createTransaction(transaction: Transaction): Result<Pair<Transaction, BudgetNotification>> {
        return transactionLocalDataSource.createTransaction(transaction = transaction)
    }

    override suspend fun getTransactionById(id: Long): Result<Transaction> {
        return transactionLocalDataSource.getTransactionById(id = id)
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Pair<Transaction, BudgetNotification>> {
        return transactionLocalDataSource.updateTransaction(transaction = transaction)
    }
}