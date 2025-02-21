package ru.crazerr.core.database.transactions.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.transactions.model.TransactionEntity

@Dao
interface TransactionsDao : BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
}