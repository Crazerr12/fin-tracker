package ru.crazerr.core.database.transactions.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.transactions.model.TransactionEntity
import ru.crazerr.core.database.transactions.model.TransactionWithAccountAndCategory
import java.time.LocalDate

@Dao
interface TransactionsDao : BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionWithAccountAndCategory

    @Transaction
    @Query("SELECT * FROM transactions WHERE date >= :startDate AND date <= :endDate")
    fun getTransactionsByPeriod(startDate: String, endDate: String): Flow<List<TransactionEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM transactions
        WHERE 
            (
                (:transactionType = 2 OR :transactionType = type) 
                AND account_id IN (:accountIds)
                AND category_id IN (:categoryIds)
            ) AND date IN (:dates)
        ORDER BY date ASC
    """
    )
    fun getTransactionsByFilters(
        accountIds: IntArray,
        categoryIds: IntArray,
        transactionType: Int,
        dates: List<String>,
    ): Flow<List<TransactionWithAccountAndCategory>>

    @Query(
        """
        SELECT DISTINCT date
        FROM transactions
        WHERE (:startDate IS NULL OR date >= :startDate) AND (:endDate IS NULL OR date <= :endDate)
        ORDER BY date DESC
        LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getPagingDates(
        limit: Int,
        offset: Int,
        startDate: String?,
        endDate: String?
    ): List<LocalDate>

    @Query(
        """
        SELECT SUM(amount) 
        FROM transactions 
        WHERE type = 1 
        AND category_id = :categoryId 
        AND strftime('%m', date) = strftime('%m', :date) 
        AND strftime('%Y', date) = strftime('%Y', :date)
    """
    )
    suspend fun getSpentAmountByDateAndCategory(categoryId: Int, date: String): Long
}