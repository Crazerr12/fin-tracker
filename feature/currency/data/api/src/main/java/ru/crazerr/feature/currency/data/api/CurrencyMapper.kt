package ru.crazerr.feature.currency.data.api

import ru.crazerr.core.database.currencies.model.CurrencyEntity
import ru.crazerr.feature.currency.domain.api.Currency


fun CurrencyEntity.toCurrency(): Currency = Currency(
    id = id,
    name = name,
    symbol = symbol,
    code = code,
)