package ru.crazerr.feature.budget.data.repository

import ru.crazerr.feature.budget.data.dataSource.BudgetLocalDataSource
import ru.crazerr.feature.budget.data.workManager.BudgetManager
import ru.crazerr.feature.budget.domain.BudgetRepository
import ru.crazerr.feature.domain.api.Budget

internal class BudgetRepositoryImpl(
    private val budgetLocalDataSource: BudgetLocalDataSource,
    private val budgetManager: BudgetManager,
) : BudgetRepository {
    override suspend fun getBudgetById(id: Long): Result<Budget> =
        budgetLocalDataSource.getBudgetById(id = id)

    override suspend fun createBudget(budget: Budget): Result<Budget> =
        budgetLocalDataSource.createBudget(budget = budget)

    override suspend fun createRepeatBudget(budget: Budget): Result<Budget> =
        budgetLocalDataSource.createRepeatBudget(budget = budget)


    override suspend fun updateBudget(budget: Budget): Result<Budget> =
        if (budget.repeatBudgetId != null) {
            val budget = budgetLocalDataSource.updateRepeatBudget(budget = budget)
            budgetManager.createNewBudget(budget = budget.getOrElse { return budget })
            budget
        } else {
            val budget = budgetLocalDataSource.updateBudget(budget = budget)
            if (budget.getOrElse { return budget }.repeatBudgetId != null) {
                budgetManager.createNewBudget(budget = budget.getOrElse { return budget })
            }
            budget
        }
}