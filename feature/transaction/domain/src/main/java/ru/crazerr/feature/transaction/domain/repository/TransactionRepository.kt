package ru.crazerr.feature.transaction.domain.repository

import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.model.BudgetNotification

interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Result<Pair<Transaction, BudgetNotification>>

    suspend fun getTransactionById(id: Long): Result<Transaction>

    suspend fun updateTransaction(transaction: Transaction): Result<Pair<Transaction, BudgetNotification>>
}