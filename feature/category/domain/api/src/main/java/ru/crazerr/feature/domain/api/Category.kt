package ru.crazerr.feature.domain.api

data class Category(
    val id: Int,
    val name: String,
    val color: Long,
    val iconModel: IconModel,
    val isTemplate: Boolean,
)

data class IconModel(
    val id: Int,
    val icon: ByteArray,
)