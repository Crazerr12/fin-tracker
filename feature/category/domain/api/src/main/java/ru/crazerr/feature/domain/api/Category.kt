package ru.crazerr.feature.domain.api

data class Category(
    val id: Int,
    val name: String,
    val color: Long,
    val icon: Icon,
    val isTemplate: Boolean,
)

data class Icon(
    val id: Int,
    val icon: ByteArray,
)