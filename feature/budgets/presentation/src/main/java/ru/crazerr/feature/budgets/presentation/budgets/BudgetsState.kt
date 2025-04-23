package ru.crazerr.feature.budgets.presentation.budgets

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.crazerr.feature.domain.api.Budget
import java.time.LocalDate

data class BudgetsState(
    val budgets: Flow<PagingData<Budget>>,
    val totalMaxBudget: Double,
    val currentMaxBudget: Double,
    val date: LocalDate,
    val totalBudgetIsLoading: Boolean,
    val bottomSheet: Boolean,
    val selectedBudget: Budget?,
)

internal val InitialBudgetsState = BudgetsState(
    budgets = emptyFlow(),
    totalMaxBudget = 0.0,
    currentMaxBudget = 0.0,
    date = LocalDate.now(),
    totalBudgetIsLoading = true,
    bottomSheet = false,
    selectedBudget = null,
)