package ru.crazerr.feature.budget.presentation.budgetStory

import ru.crazerr.feature.domain.api.Budget

sealed interface BudgetStoryComponentAction {
    data object BackClick : BudgetStoryComponentAction
    data class SaveClick(val budget: Budget) : BudgetStoryComponentAction
}