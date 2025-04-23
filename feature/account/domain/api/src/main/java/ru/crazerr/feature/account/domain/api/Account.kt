package ru.crazerr.feature.account.domain.api

import ru.crazerr.feature.currency.domain.api.Currency
import ru.crazerr.feature.icon.domain.api.IconModel

data class Account(
    val id: Long,
    val name: String,
    val amount: Double,
    val icon: IconModel,
    val currency: Currency,
)