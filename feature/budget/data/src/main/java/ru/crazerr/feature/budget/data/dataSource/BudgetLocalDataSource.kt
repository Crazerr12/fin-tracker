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

    suspend fun getBudgetById(id: Long): Result<Budget> = runCatching {
        budgetsDao.getBudgetById(id = id).toBudget()
    }

    suspend fun createBudget(budget: Budget): Result<Budget> = runCatching {
        val budgetEntity = budgetsDao.getBudgetByCategoryAndDate(
            categoryId = budget.category.id,
            date = budget.date.toString()
        )

        if (budgetEntity != null) return Result.failure(BudgetAlreadyExistException())

        budget.copy(id = budgetsDao.insert(budget.toBudgetEntity())[0])
    }

    suspend fun updateBudget(budget: Budget): Result<Budget> = runCatching {
        val id = if (budget.isRegular) {
            repeatBudgetsDao.insert(budget.toRepeatBudgetEntity())[0]
        } else null

        budgetsDao.update(budget.copy(repeatBudgetId = id).toBudgetEntity())
        budget
    }

    suspend fun createRepeatBudget(budget: Budget): Result<Budget> = runCatching {
        val repeatBudgetEntity =
            repeatBudgetsDao.getRepeatBudgetByCategory(categoryId = budget.category.id)

        if (repeatBudgetEntity != null) return Result.failure(BudgetAlreadyExistException("The regular budget has already been set for the specified category."))

        val budgetId = appDatabase.withTransaction {
            val repeatBudgetId = repeatBudgetsDao.insert(budget.toRepeatBudgetEntity())[0]
            budgetsDao.insert(budget.copy(repeatBudgetId = repeatBudgetId).toBudgetEntity())
        }[0]

        budget.copy(repeatBudgetId = budgetId)
    }

    suspend fun updateRepeatBudget(budget: Budget): Result<Budget> = runCatching {
        appDatabase.withTransaction {
            var repeatBudgetId = budget.repeatBudgetId
            if (!budget.isRegular) {
                repeatBudgetsDao.deleteById(id = repeatBudgetId!!)
                repeatBudgetId = null
            } else {
                repeatBudgetsDao.update(budget.toRepeatBudgetEntity())
            }
            budgetsDao.update(budget.copy(repeatBudgetId = repeatBudgetId).toBudgetEntity())
        }
        budget
    }
}