package ru.crazerr.feature.budget.domain

import ru.crazerr.feature.domain.api.Budget

interface BudgetRepository {
    suspend fun getBudgetById(id: Int): Result<Budget>

    suspend fun createBudget(budget: Budget): Result<Budget>

    suspend fun createRepeatBudget(budget: Budget): Result<Budget>

    suspend fun updateBudget(budget: Budget): Result<Budget>
}