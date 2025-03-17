package ru.crazerr.feature.budget.presentation.budgetStory

import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorComponentFactory
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactory

class BudgetStoryDependencies(
    val args: BudgetStoryArgs,
    val categoryEditorComponentFactory: CategoryEditorComponentFactory,
    val budgetEditorComponentFactory: BudgetEditorComponentFactory,
)