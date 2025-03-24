package ru.crazerr.core.database.budgets.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.budgets.model.BudgetEntity
import ru.crazerr.core.database.budgets.model.BudgetWithCategory
import ru.crazerr.core.database.budgets.model.TotalBudgetDto

@Dao
interface BudgetsDao : BaseDao<BudgetEntity> {

    @Query(
        """
        SELECT *
        FROM budgets
        WHERE strftime('%m', date) = strftime('%m', :date) 
        AND strftime('%Y', date) = strftime('%Y', :date)
        ORDER BY date DESC
        LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getBudgetsByDate(date: String, limit: Int, offset: Int): List<BudgetWithCategory>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): BudgetWithCategory

    @Query(
        """
            SELECT * 
            FROM budgets 
            WHERE category_id = :categoryId 
                AND strftime('%m', date) = strftime('%m', :date) 
                AND strftime('%Y', date) = strftime('%Y', :date) 
        """
    )
    suspend fun getBudgetByCategoryAndDate(categoryId: Long, date: String): BudgetEntity?

    @Query(
        """
        SELECT SUM(current_amount) as current, SUM(max_amount) as total
        FROM budgets
        WHERE strftime('%m', date) = strftime('%m', :date) 
            AND strftime('%Y', date) = strftime('%Y', :date) 
        """
    )
    fun getTotalBudget(date: String): Flow<TotalBudgetDto>
}