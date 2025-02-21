package ru.crazerr.feature.account.data.models

import ru.crazerr.core.database.currencies.model.CurrencyEntity
import ru.crazerr.feature.currency.domain.api.Currency

internal fun CurrencyEntity.toCurrency(): Currency = Currency(
    id = id,
    name = name,
    symbol = symbol,
    code = code,
)