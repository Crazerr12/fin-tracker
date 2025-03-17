package ru.crazerr.feature.budget.presentation.budgetEditor

import ru.crazerr.feature.domain.api.Budget

sealed interface BudgetEditorComponentAction {
    data object BackClick : BudgetEditorComponentAction
    data class SaveClick(val budget: Budget) : BudgetEditorComponentAction
}