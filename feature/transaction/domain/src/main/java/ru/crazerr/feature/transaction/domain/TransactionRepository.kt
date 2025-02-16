package ru.crazerr.feature.transaction.domain

import ru.crazerr.feature.transaction.domain.api.Transaction

interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Result<Transaction>
}