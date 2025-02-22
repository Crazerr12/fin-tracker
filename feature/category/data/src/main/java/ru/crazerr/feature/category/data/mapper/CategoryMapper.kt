package ru.crazerr.feature.category.data.mapper

import android.util.Log.i
import ru.crazerr.core.database.categories.model.CategoryEntity
import ru.crazerr.feature.domain.api.Category

internal fun Category.toCategoryEntity() = CategoryEntity(
    id = id,
    name = name,
    color = color,
    iconId = iconId,
    isTemplate = isTemplate
)

internal fun CategoryEntity.toCategory() = Category(
    id = id,
    name = name,
    color = color,
    iconId = iconId,
    isTemplate = isTemplate
)