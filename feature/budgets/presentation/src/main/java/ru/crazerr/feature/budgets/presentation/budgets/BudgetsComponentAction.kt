package ru.crazerr.feature.budgets.presentation.budgets

sealed interface BudgetsComponentAction {
    data class GoToBudgetEditor(val budgetId: Long) : BudgetsComponentAction
}