package ru.crazerr.core.database.currencies.dao

import androidx.room.Dao
import androidx.room.Query
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.currencies.model.CurrencyEntity

@Dao
interface CurrenciesDao : BaseDao<CurrencyEntity> {

    @Query("SELECT * FROM currencies")
    suspend fun getAllCurrencies(): List<CurrencyEntity>
}