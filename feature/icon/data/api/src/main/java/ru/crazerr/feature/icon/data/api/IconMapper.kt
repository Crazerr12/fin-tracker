package ru.crazerr.feature.icon.data.api

import ru.crazerr.core.database.icons.model.IconEntity
import ru.crazerr.feature.icon.domain.api.IconModel

fun IconModel.toIconEntity() = IconEntity(
    id = id,
    icon = icon,
    purpose = purpose,
)

fun IconEntity.toIcon() = IconModel(
    id = id,
    icon = icon,
    purpose = purpose,
)