package ru.crazerr.feature.budgets.presentation.budgetsStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryComponentFactory
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponentFactory

interface BudgetsStoryComponentFactory {
    fun create(componentContext: ComponentContext): BudgetsStoryComponent
}

internal class BudgetsStoryComponentFactoryImpl(
    private val budgetsComponentFactory: BudgetsComponentFactory,
    private val budgetStoryComponentFactory: BudgetStoryComponentFactory,
) : BudgetsStoryComponentFactory {
    override fun create(componentContext: ComponentContext): BudgetsStoryComponent =
        BudgetsStoryComponent(
            componentContext = componentContext,
            dependencies = BudgetsStoryDependencies(
                budgetStoryComponentFactory = budgetStoryComponentFactory,
                budgetsComponentFactory = budgetsComponentFactory,
            )
        )
}