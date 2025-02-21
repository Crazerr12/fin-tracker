package ru.crazerr.feature.account.data.repository

import ru.crazerr.feature.account.data.dataSource.CurrencyLocalDataSource
import ru.crazerr.feature.account.domain.repository.CurrencyRepository
import ru.crazerr.feature.currency.domain.api.Currency

internal class CurrencyRepositoryImpl(
    private val currencyLocalDataSource: CurrencyLocalDataSource,
) : CurrencyRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return currencyLocalDataSource.getCurrencies()
    }
}