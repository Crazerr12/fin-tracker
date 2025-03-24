package ru.crazerr.feature.budget.data.api

import ru.crazerr.core.database.budgets.model.BudgetEntity
import ru.crazerr.core.database.budgets.model.BudgetWithCategory
import ru.crazerr.core.database.budgets.model.RepeatBudgetEntity
import ru.crazerr.feature.category.data.api.toCategory
import ru.crazerr.feature.domain.api.Budget


fun BudgetWithCategory.toBudget() = Budget(
    id = budgetEntity.id,
    category = categoryEntity.toCategory(),
    maxAmount = budgetEntity.maxAmount,
    currentAmount = budgetEntity.currentAmount,
    repeatBudgetId = budgetEntity.repeatBudgetId,
    date = budgetEntity.date,
    isAlarm = budgetEntity.isAlarm,
    isWarning = budgetEntity.isWarning,
    isRegular = budgetEntity.repeatBudgetId != null,
)

fun Budget.toBudgetEntity() = BudgetEntity(
    id = if (id == -1L) 0 else id,
    categoryId = category.id,
    maxAmount = maxAmount,
    currentAmount = currentAmount,
    repeatBudgetId = repeatBudgetId,
    date = date,
    isAlarm = isAlarm,
    isWarning = isWarning,
)

fun Budget.toRepeatBudgetEntity() =
    RepeatBudgetEntity(
        id = repeatBudgetId ?: 0,
        categoryId = category.id,
        maxAmount = maxAmount,
        isAlarm = isAlarm,
        isWarning = isWarning,
    )