package ru.crazerr.feature.budget.presentation.budgetEditor

import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.budget.domain.BudgetRepository
import ru.crazerr.feature.budget.domain.CategoryRepository
import ru.crazerr.feature.budget.domain.TransactionRepository

class BudgetEditorDependencies(
    val args: BudgetEditorArgs,
    val transactionRepository: TransactionRepository,
    val budgetRepository: BudgetRepository,
    val categoryRepository: CategoryRepository,
    val resourceManager: ResourceManager,
)