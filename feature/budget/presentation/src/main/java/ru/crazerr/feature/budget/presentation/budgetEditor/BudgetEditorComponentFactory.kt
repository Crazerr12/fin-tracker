package ru.crazerr.feature.budget.presentation.budgetEditor

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.budget.domain.BudgetRepository
import ru.crazerr.feature.budget.domain.CategoryRepository
import ru.crazerr.feature.budget.domain.TransactionRepository

interface BudgetEditorComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (BudgetEditorComponentAction) -> Unit,
        args: BudgetEditorArgs
    ): BudgetEditorComponent
}

internal class BudgetEditorComponentFactoryImpl(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val resourceManager: ResourceManager,
) : BudgetEditorComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (BudgetEditorComponentAction) -> Unit,
        args: BudgetEditorArgs
    ): BudgetEditorComponent = BudgetEditorComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = BudgetEditorDependencies(
            args = args,
            transactionRepository = transactionRepository,
            budgetRepository = budgetRepository,
            resourceManager = resourceManager,
            categoryRepository = categoryRepository,
        )
    )
}