package ru.crazerr.feature.budgets.presentation.budgets

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.presentation.ComponentFactory
import ru.crazerr.feature.budgets.domain.repository.BudgetRepository

interface BudgetsComponentFactory : ComponentFactory<BudgetsComponent, BudgetsComponentAction>

internal class BudgetsComponentFactoryImpl(
    private val budgetRepository: BudgetRepository,
) : BudgetsComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (BudgetsComponentAction) -> Unit
    ): BudgetsComponent = BudgetsComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = BudgetsDependencies(
            budgetRepository = budgetRepository,
        )
    )
}