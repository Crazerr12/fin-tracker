package ru.crazerr.feature.account.data.dataSource

import ru.crazerr.core.database.currencies.dao.CurrenciesDao
import ru.crazerr.feature.account.data.models.toCurrency
import ru.crazerr.feature.currency.domain.api.Currency

internal class CurrencyLocalDataSource(
    private val currenciesDao: CurrenciesDao,
) {
    suspend fun getCurrencies(): Result<List<Currency>> =
        try {
            val currencyEntities = currenciesDao.getAllCurrencies()

            Result.success(currencyEntities.map { it.toCurrency() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
}