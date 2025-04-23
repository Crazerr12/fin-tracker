package ru.crazerr.core.database.transactions.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.categories.model.CategoryWithIconAndSum
import ru.crazerr.core.database.transactions.model.TransactionEntity
import ru.crazerr.core.database.transactions.model.TransactionWithAccountAndCategory
import java.time.LocalDate

@Dao
interface TransactionsDao : BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionWithAccountAndCategory

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
        accountIds: LongArray,
        categoryIds: LongArray,
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
    suspend fun getSpentAmountByDateAndCategory(categoryId: Long, date: String): Double

    @Query(
        """
        SELECT SUM(amount)
        FROM transactions 
        WHERE date BETWEEN :startDate and :endDate
        AND type = :transactionType
        """
    )
    fun getTotalAmountByPeriodAndType(
        transactionType: Int,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<Double>

    @Query(
        """
            SELECT c.*, i.*, SUM(t.amount) as total_sum
            FROM transactions t
            JOIN categories c ON t.category_id = c.id
            JOIN icons i ON c.icon_id = i.id
            WHERE t.date BETWEEN :startDate AND :endDate
            AND t.type = :transactionType
            GROUP BY t.category_id
            ORDER BY total_sum DESC
        """
    )
    fun getAnalysisCategoriesByDateAndType(
        startDate: LocalDate,
        endDate: LocalDate,
        transactionType: Int,
    ): Flow<List<CategoryWithIconAndSum>>
}