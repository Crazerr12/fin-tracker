package ru.crazerr.core.database.transactions.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.transactions.model.TransactionEntity
import ru.crazerr.core.database.transactions.model.TransactionWithAccountAndCategory

@Dao
interface TransactionsDao : BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionWithAccountAndCategory

    @Transaction
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    fun getTransactionsByPeriod(startDate: String, endDate: String): Flow<List<TransactionEntity>>
}