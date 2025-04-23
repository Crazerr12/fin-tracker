package ru.crazerr.feature.budgets.data.dataSource

import androidx.room.InvalidationTracker
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.budget.data.api.toBudget
import ru.crazerr.feature.budget.data.api.toBudgetEntity
import ru.crazerr.feature.budgets.data.mapper.toTotalBudget
import ru.crazerr.feature.budgets.domain.model.TotalBudget
import ru.crazerr.feature.domain.api.Budget
import java.time.LocalDate

internal class BudgetLocalDataSource(
    private val appDatabase: AppDatabase,
) {
    private val budgetsDao = appDatabase.budgetsDao()
    private val repeatBudgetsDao = appDatabase.repeatBudgetsDao()
    private val invalidationTracker = appDatabase.invalidationTracker

    suspend fun getBudgetsForCurrentDate(
        date: LocalDate,
        limit: Int,
        offset: Int,
    ): Result<List<Budget>> = runCatching {
        budgetsDao.getBudgetsByDate(date = date.toString(), limit = limit, offset = offset)
            .map { it.toBudget() }
    }

    fun getTotalBudget(date: LocalDate): Result<Flow<TotalBudget>> = runCatching {
        budgetsDao.getTotalBudget(date = date.toString()).map { it.toTotalBudget() }
    }

    suspend fun deleteBudget(budget: Budget): Result<Budget> = runCatching {
        appDatabase.withTransaction {
            budgetsDao.delete(budget.toBudgetEntity())
            if (budget.repeatBudgetId != null) {
                repeatBudgetsDao.deleteById(id = budget.repeatBudgetId!!)
            }
            budget
        }
    }

    fun addObserver(observer: InvalidationTracker.Observer) {
        invalidationTracker.addObserver(observer)
    }

    fun removeObserver(observer: InvalidationTracker.Observer) {
        invalidationTracker.removeObserver(observer)
    }
}