package ru.crazerr.feature.currency.domain.api

data class Currency(
    val id: Long,
    val name: String,
    val symbol: String,
    val code: String,
)