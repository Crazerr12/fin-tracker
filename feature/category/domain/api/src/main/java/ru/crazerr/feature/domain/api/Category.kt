package ru.crazerr.feature.domain.api

data class Category(
    val id: Long,
    val name: String,
    val color: Long,
    val iconModel: IconModel,
    val isTemplate: Boolean,
)

data class IconModel(
    val id: Long,
    val icon: ByteArray,
)