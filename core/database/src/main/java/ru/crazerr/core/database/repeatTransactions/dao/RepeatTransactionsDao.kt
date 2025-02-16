package ru.crazerr.core.database.repeatTransactions.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.repeatTransactions.model.RepeatTransactionEntity

@Dao
interface RepeatTransactionsDao : BaseDao<RepeatTransactionEntity> {
    @Query("SELECT * FROM repeat_transactions")
    fun getAllRepeatTransactions(): Flow<RepeatTransactionEntity>
}