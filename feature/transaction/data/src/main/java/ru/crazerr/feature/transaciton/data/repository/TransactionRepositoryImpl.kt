package ru.crazerr.feature.transaciton.data.repository

import ru.crazerr.feature.transaciton.data.dataSource.RemoteTransactionDataSource
import ru.crazerr.feature.transaction.domain.TransactionRepository
import ru.crazerr.feature.transaction.domain.api.Transaction

internal class TransactionRepositoryImpl(
    private val remoteTransactionDataSource: RemoteTransactionDataSource,
) : TransactionRepository {
    override suspend fun createTransaction(transaction: Transaction): Result<Transaction> {
        remoteTransactionDataSource.createTransaction()
    }
}