package ru.crazerr.feature.budget.presentation.budgetEditor

import ru.crazerr.feature.domain.api.Category

sealed interface BudgetEditorViewAction {
    data object BackClick : BudgetEditorViewAction

    data object ManageCategoriesDropdown : BudgetEditorViewAction
    data class UpdateSelectedCategory(val category: Category) : BudgetEditorViewAction

    data class UpdateMaxAmount(val maxAmount: String) : BudgetEditorViewAction

    data object CheckIsRegular : BudgetEditorViewAction
    data object CheckAlarmBudgetExceeded : BudgetEditorViewAction
    data object CheckWarningBudgetClose : BudgetEditorViewAction

    data object SaveClick : BudgetEditorViewAction
}