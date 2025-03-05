package ru.crazerr.feature.transaction.domain

import ru.crazerr.feature.transaction.domain.api.Transaction

interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Result<Transaction>

    suspend fun getTransactionById(id: Int): Result<Transaction>

    suspend fun updateTransaction(transaction: Transaction): Result<Transaction>
}