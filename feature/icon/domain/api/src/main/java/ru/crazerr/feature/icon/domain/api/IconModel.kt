package ru.crazerr.feature.icon.domain.api


data class IconModel(
    val id: Long,
    val icon: ByteArray,
    val purpose: String,
)