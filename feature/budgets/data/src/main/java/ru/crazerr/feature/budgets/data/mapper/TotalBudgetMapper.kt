package ru.crazerr.feature.budgets.data.mapper

import ru.crazerr.core.database.budgets.model.TotalBudgetDto
import ru.crazerr.feature.budgets.domain.model.TotalBudget

fun TotalBudgetDto.toTotalBudget() = TotalBudget(
    total = total,
    current = current,
)