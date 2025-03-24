package ru.crazerr.feature.account.domain.api

import ru.crazerr.feature.currency.domain.api.Currency

data class Account(
    val id: Long,
    val name: String,
    val amount: Double,
    val iconId: String,
    val currency: Currency,
)