package ru.crazerr.core.database.currencies.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.currencies.model.CurrencyEntity

@Dao
interface CurrenciesDao: BaseDao<CurrencyEntity> {

    @Query("SELECT * FROM currencies")
    fun getAllCurrencies(): Flow<CurrencyEntity>
}