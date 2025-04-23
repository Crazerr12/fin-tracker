package ru.crazerr.feature.budgets.presentation.budgets

import ru.crazerr.feature.domain.api.Budget

sealed interface BudgetsViewAction {
    data object PreviousMonthClick : BudgetsViewAction
    data object NextMonthClick : BudgetsViewAction

    data class GoToBudgetEditor(val budgetId: Long = -1) : BudgetsViewAction

    data class SelectBudget(val budget: Budget?) : BudgetsViewAction
    data object DeleteSelectedBudget : BudgetsViewAction
}