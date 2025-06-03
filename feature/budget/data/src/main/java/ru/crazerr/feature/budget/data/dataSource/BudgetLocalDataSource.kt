package ru.crazerr.feature.budget.data.dataSource

import androidx.room.withTransaction
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.budget.data.api.toBudget
import ru.crazerr.feature.budget.data.api.toBudgetEntity
import ru.crazerr.feature.budget.data.api.toRepeatBudgetEntity
import ru.crazerr.feature.budget.data.exception.BudgetAlreadyExistException
import ru.crazerr.feature.domain.api.Budget

internal class BudgetLocalDataSource(
    private val appDatabase: AppDatabase,
) {
    private val budgetsDao = appDatabase.budgetsDao()
    private val repeatBudgetsDao = appDatabase.repeatBudgetsDao()
    private val transactionsDao = appDatabase.transactionsDao()

    suspend fun getBudgetById(id: Long): Result<Budget> = runCatching {
        budgetsDao.getBudgetById(id = id).toBudget()
    }

    suspend fun createBudget(budget: Budget): Result<Budget> = runCatching {
        val budgetEntity = budgetsDao.getBudgetByCategoryAndDate(
            categoryId = budget.category.id,
            date = budget.date.toString(),
        )

        if (budgetEntity != null) return Result.failure(BudgetAlreadyExistException())

        appDatabase.withTransaction {
            val transactions = transactionsDao.getTransactionsByPeriodAndCategory(
                date = budget.date.toString(),
                categories = listOf(budget.category.id),
            )

            val id = budgetsDao.insert(budget.toBudgetEntity())[0]
            transactionsDao.update(*transactions.map { it.copy(budgetId = id) }.toTypedArray())

            budget.copy(id = id)
        }
    }

    suspend fun updateBudget(budget: Budget): Result<Budget> = runCatching {
        appDatabase.withTransaction {
            val oldBudget = budgetsDao.getBudgetById(id = budget.id)
            val repeatBudgetId = if (budget.isRegular && budget.repeatBudgetId == null) {
                repeatBudgetsDao.insert(budget.toRepeatBudgetEntity())[0]
            } else budget.repeatBudgetId

            budgetsDao.update(budget.copy(repeatBudgetId = repeatBudgetId).toBudgetEntity())

            if (oldBudget.categoryId != budget.category.id) {
                val transactions = transactionsDao.getTransactionsByPeriodAndCategory(
                    date = budget.date.toString(),
                    categories = listOf(oldBudget.categoryId, budget.category.id),
                )

                val updatedTransactions = transactions.map {
                    it.copy(
                        budgetId = if (it.categoryId == oldBudget.categoryId) null else budget.id
                    )
                }

                transactionsDao.update(*updatedTransactions.toTypedArray())
            }
            budget
        }
    }

    suspend fun createRepeatBudget(budget: Budget): Result<Budget> = runCatching {
        val repeatBudgetEntity =
            repeatBudgetsDao.getRepeatBudgetByCategory(categoryId = budget.category.id)

        if (repeatBudgetEntity != null) return Result.failure(BudgetAlreadyExistException("The regular budget has already been set for the specified category"))

        val (budgetId, repeatBudgetId) = appDatabase.withTransaction {
            val repeatBudgetId = repeatBudgetsDao.insert(budget.toRepeatBudgetEntity())[0]
            val budgetId =
                createBudget(budget = budget.copy(repeatBudgetId = repeatBudgetId)).getOrDefault(
                    budget
                ).id
            Pair(budgetId, repeatBudgetId)
        }

        budget.copy(id = budgetId, repeatBudgetId = repeatBudgetId)
    }

    suspend fun updateRepeatBudget(budget: Budget): Result<Budget> = runCatching {
        var repeatBudgetId = budget.repeatBudgetId
        appDatabase.withTransaction {
            if (!budget.isRegular) {
                repeatBudgetsDao.deleteById(id = repeatBudgetId!!)
                repeatBudgetId = null
            } else {
                repeatBudgetsDao.update(budget.toRepeatBudgetEntity())
            }

            val updateBudgetResult =
                updateBudget(budget = budget.copy(repeatBudgetId = repeatBudgetId))

            if (updateBudgetResult.isFailure) {
                return@withTransaction updateBudgetResult
            }
        }
        budget.copy(repeatBudgetId = repeatBudgetId)
    }
}