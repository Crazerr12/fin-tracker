package ru.crazerr.feature.category.presentation.api.model

import kotlinx.serialization.Serializable
import ru.crazerr.feature.icon.presentation.api.model.IconUi

@Serializable
data class CategoryUi(
    val id: Long,
    val name: String,
    val color: Long,
    val iconUi: IconUi,
    val isTemplate: Boolean,
)