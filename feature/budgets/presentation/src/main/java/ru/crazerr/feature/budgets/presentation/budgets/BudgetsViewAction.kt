package ru.crazerr.feature.budgets.presentation.budgets

sealed interface BudgetsViewAction {
    data object PreviousMonthClick : BudgetsViewAction
    data object NextMonthClick : BudgetsViewAction

    data class GoToBudgetEditor(val budgetId: Long = -1) : BudgetsViewAction
}