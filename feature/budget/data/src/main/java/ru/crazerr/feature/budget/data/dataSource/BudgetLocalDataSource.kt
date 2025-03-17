package ru.crazerr.feature.budget.data.dataSource

import androidx.room.withTransaction
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.budget.data.api.toBudget
import ru.crazerr.feature.budget.data.api.toBudgetEntity
import ru.crazerr.feature.budget.data.api.toRepeatBudgetEntity
import ru.crazerr.feature.domain.api.Budget

internal class BudgetLocalDataSource(
    private val appDatabase: AppDatabase,
) {
    private val budgetsDao = appDatabase.budgetsDao()
    private val repeatBudgetsDao = appDatabase.repeatBudgetsDao()

    suspend fun getBudgetById(id: Int): Result<Budget> = runCatching {
        budgetsDao.getBudgetById(id = id).toBudget()
    }

    suspend fun createBudget(budget: Budget): Result<Budget> = runCatching {
        budget.copy(id = budgetsDao.insert(budget.toBudgetEntity())[0].toInt())
    }

    suspend fun updateBudget(budget: Budget): Result<Budget> = runCatching {
        budgetsDao.update(budget.toBudgetEntity())
        budget
    }

    suspend fun createRepeatBudget(budget: Budget): Result<Budget> = runCatching {
        val budgetId = appDatabase.withTransaction {
            val repeatBudgetId = repeatBudgetsDao.insert(budget.toRepeatBudgetEntity())[0].toInt()
            budgetsDao.insert(budget.copy(repeatBudgetId = repeatBudgetId).toBudgetEntity())
        }[0].toInt()

        budget.copy(repeatBudgetId = budgetId)
    }

    suspend fun updateRepeatBudget(budget: Budget): Result<Budget> = runCatching {
        appDatabase.withTransaction {
            repeatBudgetsDao.update(budget.toRepeatBudgetEntity())
            budgetsDao.update(budget.toBudgetEntity())
        }
        budget
    }
}