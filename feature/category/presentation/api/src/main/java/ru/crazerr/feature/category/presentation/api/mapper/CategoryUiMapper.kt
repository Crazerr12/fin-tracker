package ru.crazerr.feature.category.presentation.api.mapper

import ru.crazerr.feature.category.domain.api.Category
import ru.crazerr.feature.category.presentation.api.model.CategoryUi
import ru.crazerr.feature.icon.presentation.api.mapper.toIconModel
import ru.crazerr.feature.icon.presentation.api.mapper.toIconUi

fun Category.toCategoryUi() = CategoryUi(
    id = id,
    name = name,
    color = color,
    iconUi = iconModel.toIconUi(),
    isTemplate = isTemplate,
)

fun CategoryUi.toCategory() = Category(
    id = id,
    name = name,
    color = color,
    iconModel = iconUi.toIconModel(),
    isTemplate = isTemplate,
)
