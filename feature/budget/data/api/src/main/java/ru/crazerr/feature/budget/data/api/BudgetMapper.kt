package ru.crazerr.feature.budget.data.api

import ru.crazerr.core.database.budgets.model.BudgetEntity
import ru.crazerr.core.database.budgets.model.BudgetWithCategory
import ru.crazerr.core.database.budgets.model.RepeatBudgetEntity
import ru.crazerr.feature.domain.api.Budget
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.icon.domain.api.IconModel


fun BudgetWithCategory.toBudget() = Budget(
    id = id,
    category = Category(
        id = categoryId,
        name = categoryName,
        color = categoryColor,
        iconModel = IconModel(id = iconId, icon = icon, purpose = iconPurpose),
        isTemplate = categoryIsTemplate,
    ),
    maxAmount = maxAmount,
    currentAmount = currentAmount,
    repeatBudgetId = repeatBudgetId,
    date = date,
    isAlarm = isAlarm,
    isWarning = isWarning,
    isRegular = repeatBudgetId != null,
)

fun Budget.toBudgetEntity() = BudgetEntity(
    id = if (id == -1L) 0 else id,
    categoryId = category.id,
    maxAmount = maxAmount,
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