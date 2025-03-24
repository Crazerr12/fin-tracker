package ru.crazerr.feature.budgets.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.budgets.domain.model.TotalBudget
import ru.crazerr.feature.domain.api.Budget
import java.time.LocalDate

interface BudgetRepository {
    fun getBudgetsForCurrentDate(date: LocalDate): Flow<PagingData<Budget>>

    fun getTotalBudget(date: LocalDate): Result<Flow<TotalBudget>>
}