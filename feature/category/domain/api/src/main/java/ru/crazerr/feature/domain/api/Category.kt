package ru.crazerr.feature.domain.api

data class Category(
    val id: Int,
    val name: String,
    val color: Long,
    val iconId: String,
    val isTemplate: Boolean,
)