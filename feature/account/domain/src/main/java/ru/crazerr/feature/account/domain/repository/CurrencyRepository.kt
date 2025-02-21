package ru.crazerr.feature.account.domain.repository

import ru.crazerr.feature.currency.domain.api.Currency

interface CurrencyRepository {
    suspend fun getCurrencies(): Result<List<Currency>>
}