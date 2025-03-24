package ru.crazerr.feature.budgets.presentation.budgetsStory

import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryComponentFactory
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponentFactory

class BudgetsStoryDependencies(
    val budgetStoryComponentFactory: BudgetStoryComponentFactory,
    val budgetsComponentFactory: BudgetsComponentFactory,
)