package ru.crazerr.feature.budgets.presentation.budgets

import ru.crazerr.feature.budgets.domain.repository.BudgetRepository

class BudgetsDependencies(
    val budgetRepository: BudgetRepository,
)