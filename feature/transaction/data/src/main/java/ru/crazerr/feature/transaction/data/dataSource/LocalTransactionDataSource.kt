package ru.crazerr.feature.transaction.data.dataSource

import androidx.room.withTransaction
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.transaction.data.api.toTransaction
import ru.crazerr.feature.transaction.data.api.toTransactionEntity
import ru.crazerr.feature.transaction.domain.api.Transaction

internal class LocalTransactionDataSource(
    private val appDatabase: AppDatabase,
) {
    suspend fun getTransactionById(id: Int): Result<Transaction> = try {
        val transactionEntity = appDatabase.transactionsDao().getTransactionById(id = id)

        Result.success(transactionEntity.toTransaction())
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    suspend fun createTransaction(transaction: Transaction): Result<Transaction> = try {
        val id: Int = appDatabase.withTransaction {
            val transactionId =
                appDatabase.transactionsDao().insert(transaction.toTransactionEntity())[0].toInt()

            appDatabase.accountsDao().updateAccountBalance(
                id = transaction.account.id,
                transactionAmount = -transaction.amount
            )

            transactionId
        }

        Result.success(transaction.copy(id = id))
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    suspend fun updateTransaction(transaction: Transaction): Result<Transaction> = try {
        val oldTransaction = appDatabase.transactionsDao().getTransactionById(id = transaction.id)

        appDatabase.withTransaction {
            if (oldTransaction.transaction.accountId != transaction.account.id) {
                appDatabase.accountsDao()
                    .updateAccountBalance(
                        id = oldTransaction.transaction.accountId,
                        transactionAmount = oldTransaction.transaction.amount,
                    )
            }

            appDatabase.transactionsDao().update(transaction.toTransactionEntity())

            appDatabase.accountsDao().updateAccountBalance(
                id = transaction.account.id,
                transactionAmount = -transaction.amount,
            )
        }

        Result.success(transaction)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}