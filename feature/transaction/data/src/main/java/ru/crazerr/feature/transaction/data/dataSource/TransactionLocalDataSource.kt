package ru.crazerr.feature.transaction.data.dataSource

import android.util.Log
import androidx.room.withTransaction
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.transactions.model.TransactionWithAccountAndCategory
import ru.crazerr.feature.transaction.data.api.toTransaction
import ru.crazerr.feature.transaction.data.api.toTransactionEntity
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transaction.domain.model.BudgetNotification

internal class TransactionLocalDataSource(
    private val appDatabase: AppDatabase,
) {
    private val transactionsDao = appDatabase.transactionsDao()
    private val accountsDao = appDatabase.accountsDao()
    private val budgetsDao = appDatabase.budgetsDao()

    suspend fun getTransactionById(id: Long): Result<Transaction> = runCatching {
        transactionsDao.getTransactionById(id = id).toTransaction()
    }

    suspend fun createTransaction(transaction: Transaction): Result<Pair<Transaction, BudgetNotification>> =
        try {
            val budget = if (transaction.type == TransactionType.Expense) {
                budgetsDao.getBudgetByCategoryAndDate(
                    categoryId = transaction.category.id,
                    date = transaction.date.toString(),
                )
            } else null

            val updatedTransaction = transaction.copy(budgetId = budget?.id)
            val id = appDatabase.withTransaction {
                val transactionId =
                    transactionsDao.insert(updatedTransaction.toTransactionEntity())[0]

                accountsDao.updateAccountBalance(
                    id = updatedTransaction.account.id,
                    transactionAmount = if (updatedTransaction.type == TransactionType.Income) updatedTransaction.amount else -updatedTransaction.amount,
                )

                transactionId
            }

            Result.success(
                updatedTransaction.copy(id = id) to BudgetNotification(
                    isWarning = budget?.isWarning == true,
                    isAlarm = budget?.isAlarm == true,
                    percentage = if (budget != null) (budget.currentAmount + updatedTransaction.amount) / budget.maxAmount else 0.0
                )
            )
        } catch (ex: Exception) {
            Log.e("CreateTransactionError", ex.localizedMessage ?: "")
            Result.failure(ex)
        }

    suspend fun updateTransaction(transaction: Transaction): Result<Pair<Transaction, BudgetNotification>> =
        runCatching {
            val oldTransaction = transactionsDao.getTransactionById(id = transaction.id)

            val budget = if (transaction.type == TransactionType.Expense ||
                TransactionType.fromId(id = oldTransaction.transaction.type) == TransactionType.Expense
            ) {
                budgetsDao.getBudgetByCategoryAndDate(
                    categoryId = transaction.category.id,
                    date = transaction.date.toString(),
                )
            } else null

            val updatedTransaction = transaction.copy(budgetId = budget?.id)

            appDatabase.withTransaction {
                updateOldAccount(oldTransaction = oldTransaction, transaction = updatedTransaction)

                transactionsDao.update(
                    updatedTransaction.toTransactionEntity()
                )

                updateCurrentAccount(
                    oldTransaction = oldTransaction,
                    transaction = updatedTransaction,
                )
            }

            updatedTransaction to BudgetNotification(
                isWarning = budget?.isWarning == true,
                isAlarm = budget?.isAlarm == true,
                percentage = if (budget != null) {
                    (budget.currentAmount + getAmountForUpdateBudget(
                        oldTransaction = oldTransaction,
                        transaction = updatedTransaction,
                    )) / budget.maxAmount
                } else 0.0
            )
        }

    private suspend fun updateOldAccount(
        oldTransaction: TransactionWithAccountAndCategory,
        transaction: Transaction,
    ) {
        if (oldTransaction.transaction.accountId != transaction.account.id) {
            accountsDao.updateAccountBalance(
                id = oldTransaction.transaction.accountId,
                transactionAmount = if (TransactionType.fromId(oldTransaction.transaction.type) != TransactionType.Income) {
                    oldTransaction.transaction.amount
                } else {
                    -oldTransaction.transaction.amount
                },
            )
        }
    }

    private suspend fun updateCurrentAccount(
        oldTransaction: TransactionWithAccountAndCategory,
        transaction: Transaction,
    ) {
        val isSameAccount = oldTransaction.transaction.accountId == transaction.account.id
        val oldTransactionType = TransactionType.fromId(oldTransaction.transaction.type)
        val amount = when {
            oldTransactionType == TransactionType.Income && transaction.type == TransactionType.Income -> {
                if (isSameAccount) transaction.amount - oldTransaction.transaction.amount
                else transaction.amount
            }

            oldTransactionType == TransactionType.Income && transaction.type == TransactionType.Expense -> {
                if (isSameAccount) -(oldTransaction.transaction.amount + transaction.amount)
                else -transaction.amount
            }

            oldTransactionType == TransactionType.Expense && transaction.type == TransactionType.Income -> {
                if (isSameAccount) oldTransaction.transaction.amount + transaction.amount
                else transaction.amount
            }

            oldTransactionType == TransactionType.Expense && transaction.type == TransactionType.Expense -> {
                if (isSameAccount) -(transaction.amount - oldTransaction.transaction.amount)
                else -transaction.amount

            }

            else -> 0.0
        }

        if (amount != 0.0) {
            accountsDao.updateAccountBalance(
                id = transaction.account.id,
                transactionAmount = amount,
            )
        }
    }

    private fun getAmountForUpdateBudget(
        oldTransaction: TransactionWithAccountAndCategory,
        transaction: Transaction,
    ): Double {
        val oldTransactionType = TransactionType.fromId(oldTransaction.transaction.type)

        return when {
            oldTransactionType == TransactionType.Expense && transaction.type == TransactionType.Expense -> {
                transaction.amount - oldTransaction.transaction.amount
            }

            oldTransactionType == TransactionType.Expense -> {
                -oldTransaction.transaction.amount
            }

            transaction.type == TransactionType.Expense -> {
                transaction.amount
            }

            else -> 0.0
        }
    }
}