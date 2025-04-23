package ru.crazerr.feature.category.data.api

import ru.crazerr.core.database.categories.model.CategoryEntity
import ru.crazerr.core.database.categories.model.CategoryWithIcon
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.icon.data.api.toIcon

fun Category.toCategoryEntity() = CategoryEntity(
    id = id,
    name = name,
    color = color,
    iconId = iconModel.id,
    isTemplate = isTemplate
)

fun CategoryWithIcon.toCategory() = Category(
    id = categoryEntity.id,
    name = categoryEntity.name,
    color = categoryEntity.color,
    iconModel = iconEntity.toIcon(),
    isTemplate = categoryEntity.isTemplate,
)