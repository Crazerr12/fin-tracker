package ru.crazerr.feature.budget.presentation.budgetStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorComponentFactory
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactory

interface BudgetStoryComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (BudgetStoryComponentAction) -> Unit,
        args: BudgetStoryArgs
    ): BudgetStoryComponent
}

internal class BudgetStoryComponentFactoryImpl(
    private val budgetEditorComponentFactory: BudgetEditorComponentFactory,
    private val categoryEditorComponentFactory: CategoryEditorComponentFactory,
) : BudgetStoryComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (BudgetStoryComponentAction) -> Unit,
        args: BudgetStoryArgs
    ): BudgetStoryComponent = BudgetStoryComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = BudgetStoryDependencies(
            args = args,
            categoryEditorComponentFactory = categoryEditorComponentFactory,
            budgetEditorComponentFactory = budgetEditorComponentFactory,
        ),
    )
}