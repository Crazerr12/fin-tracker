package ru.crazerr.feature.transaction.data.model

import ru.crazerr.core.database.categories.model.CategoryEntity
import ru.crazerr.feature.domain.api.Category

internal fun CategoryEntity.toCategory() = Category(
    id = id,
    name = name,
    color = color,
    iconId = iconId,
    isTemplate = isTemplate,
)

