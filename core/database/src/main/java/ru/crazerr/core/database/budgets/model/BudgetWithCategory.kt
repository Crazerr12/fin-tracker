package ru.crazerr.core.database.budgets.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.crazerr.core.database.categories.model.CategoryEntity
import ru.crazerr.core.database.categories.model.CategoryWithIcon

data class BudgetWithCategory(
    @Embedded
    val budgetEntity: BudgetEntity,
    @Relation(entity = CategoryEntity::class, parentColumn = "category_id", entityColumn = "id")
    val categoryEntity: CategoryWithIcon,
)