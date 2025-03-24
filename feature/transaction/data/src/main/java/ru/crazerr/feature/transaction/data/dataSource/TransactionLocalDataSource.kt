package ru.crazerr.feature.transaction.data.dataSource

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
        runCatching {
            val budgetEntity = if (transaction.type == TransactionType.Expense) {
                budgetsDao.getBudgetByCategoryAndDate(
                    categoryId = transaction.category.id,
                    date = transaction.date.toString(),
                )
            } else null

            val id = appDatabase.withTransaction {
                val transactionId =
                    transactionsDao.insert(transaction.toTransactionEntity())[0]

                accountsDao.updateAccountBalance(
                    id = transaction.account.id,
                    transactionAmount = if (transaction.type == TransactionType.Income) transaction.amount else -transaction.amount,
                )

                if (budgetEntity != null) {
                    budgetsDao.update(budgetEntity.copy(currentAmount = budgetEntity.currentAmount + transaction.amount))
                }

                transactionId
            }

            transaction.copy(id = id) to BudgetNotification(
                isWarning = budgetEntity?.isWarning == true,
                isAlarm = budgetEntity?.isAlarm == true,
                percentage = if (budgetEntity != null) (budgetEntity.currentAmount + transaction.amount) / budgetEntity.maxAmount else 0.0
            )
        }

    suspend fun updateTransaction(transaction: Transaction): Result<Pair<Transaction, BudgetNotification>> =
        runCatching {
            val oldTransaction = transactionsDao.getTransactionById(id = transaction.id)

            val budgetEntity = if (transaction.type == TransactionType.Expense ||
                TransactionType.fromId(id = oldTransaction.transaction.type) == TransactionType.Expense
            ) {
                budgetsDao.getBudgetByCategoryAndDate(
                    categoryId = transaction.category.id,
                    date = transaction.date.toString(),
                )
            } else null

            val amount =
                getAmountForUpdateBudget(oldTransaction = oldTransaction, transaction = transaction)

            appDatabase.withTransaction {
                updateOldAccount(oldTransaction = oldTransaction, transaction = transaction)

                transactionsDao.update(transaction.toTransactionEntity())

                updateCurrentAccount(oldTransaction = oldTransaction, transaction = transaction)

                if (budgetEntity != null && amount != 0.0) {
                    budgetsDao.update(budgetEntity.copy(currentAmount = budgetEntity.currentAmount + amount))
                }
            }

            transaction to BudgetNotification(
                isWarning = budgetEntity?.isWarning == true,
                isAlarm = budgetEntity?.isAlarm == true,
                percentage = if (budgetEntity != null) (budgetEntity.currentAmount + amount) / budgetEntity.maxAmount else 0.0
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
                transactionAmount = amount
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