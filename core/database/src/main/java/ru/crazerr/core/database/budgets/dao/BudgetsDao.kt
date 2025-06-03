package ru.crazerr.core.database.budgets.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.budgets.model.BudgetEntity
import ru.crazerr.core.database.budgets.model.BudgetWithCategory
import ru.crazerr.core.database.budgets.model.TotalBudgetDto

@Dao
interface BudgetsDao : BaseDao<BudgetEntity> {

    @Transaction
    @Query(
        """
        SELECT b.*,
            COALESCE(SUM(t.amount),0) AS current_amount,
            c.name AS category_name, c.color AS category_color, c.is_template AS category_is_template,
            i.id AS icon_id, i.icon AS icon, i.purpose AS icon_purpose
        FROM budgets b
        LEFT JOIN transactions t ON t.budget_id = b.id
        INNER JOIN categories c ON c.id = b.category_id
        INNER JOIN icons i ON i.id = c.icon_id
        WHERE strftime('%m', b.date) = strftime('%m', :date) 
        AND strftime('%Y', b.date) = strftime('%Y', :date)
        GROUP BY b.id
        ORDER BY b.date DESC
        LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getBudgetsByDate(date: String, limit: Int, offset: Int): List<BudgetWithCategory>

    @Transaction
    @Query(
        """
        SELECT b.*,
            COALESCE(SUM(t.amount),0) AS current_amount,
            c.name AS category_name, c.color AS category_color, c.is_template AS category_is_template,
            i.id AS icon_id, i.icon AS icon, i.purpose AS icon_purpose
        FROM budgets b
        LEFT JOIN transactions t ON t.budget_id = b.id
        INNER JOIN categories c ON c.id = b.category_id
        INNER JOIN icons i ON i.id = c.icon_id
        WHERE b.id = :id
        GROUP BY b.id
        """
    )
    suspend fun getBudgetById(id: Long): BudgetWithCategory

    @Query(
        """
        SELECT b.*, 
            COALESCE(SUM(t.amount),0) AS current_amount,
            c.name AS category_name, c.color AS category_color, c.is_template AS category_is_template,
            i.id AS icon_id, i.icon AS icon, i.purpose AS icon_purpose
        FROM budgets b
        LEFT JOIN transactions t ON t.budget_id = b.id
        INNER JOIN categories c ON c.id = b.category_id
        INNER JOIN icons i ON i.id = c.icon_id
        WHERE b.category_id = :categoryId 
            AND strftime('%m', b.date) = strftime('%m', :date) 
            AND strftime('%Y', b.date) = strftime('%Y', :date) 
        GROUP BY b.id
        """
    )
    suspend fun getBudgetByCategoryAndDate(categoryId: Long, date: String): BudgetWithCategory?

    @Query(
        """
        SELECT 
            (SELECT SUM(max_amount) FROM budgets 
             WHERE strftime('%m', date) = strftime('%m', :date) 
               AND strftime('%Y', date) = strftime('%Y', :date)) AS total,
            
            (SELECT COALESCE(SUM(t.amount), 0)
             FROM budgets b
             LEFT JOIN transactions t ON t.budget_id = b.id
             WHERE strftime('%m', b.date) = strftime('%m', :date) 
               AND strftime('%Y', b.date) = strftime('%Y', :date)) AS current
    """
    )
    fun getTotalBudget(date: String): Flow<TotalBudgetDto>
}
