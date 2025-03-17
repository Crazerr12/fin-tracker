package ru.crazerr.feature.budget.presentation.budgetEditor

import kotlinx.coroutines.flow.SharedFlow

class BudgetEditorArgs(
    val id: Int = -1,
    val inputFlow: SharedFlow<BudgetEditorComponent.Input>,
)