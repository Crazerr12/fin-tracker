package ru.crazerr.feature.icon.presentation.api.model

import kotlinx.serialization.Serializable

@Serializable
data class IconUi(
    val id: Long,
    val icon: ByteArray,
    val purpose: String,
)