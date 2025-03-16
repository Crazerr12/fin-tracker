package ru.crazerr.feature.category.data.api

import ru.crazerr.core.database.icons.model.IconEntity
import ru.crazerr.feature.domain.api.Icon

fun Icon.toIconEntity() = IconEntity(
    id = id,
    icon = icon,
)

fun IconEntity.toIcon() = Icon(
    id = id,
    icon = icon,
)