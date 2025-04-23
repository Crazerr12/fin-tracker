package ru.crazerr.feature.domain.api

import ru.crazerr.feature.icon.domain.api.IconModel

data class Category(
    val id: Long,
    val name: String,
    val color: Long,
    val iconModel: IconModel,
    val isTemplate: Boolean,
)