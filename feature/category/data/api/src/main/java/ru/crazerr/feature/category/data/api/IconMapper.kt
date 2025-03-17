package ru.crazerr.feature.category.data.api

import ru.crazerr.core.database.icons.model.IconEntity
import ru.crazerr.feature.domain.api.IconModel

fun IconModel.toIconEntity() = IconEntity(
    id = id,
    icon = icon,
)

fun IconEntity.toIcon() = IconModel(
    id = id,
    icon = icon,
)