package ru.crazerr.core.database.budgets.dao

import androidx.room.Dao
import androidx.room.Query
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.budgets.model.RepeatBudgetEntity

@Dao
interface RepeatBudgetsDao : BaseDao<RepeatBudgetEntity> {
    @Query("SELECT * FROM repeat_budgets WHERE category_id = :categoryId")
    suspend fun getRepeatBudgetByCategory(categoryId: Long): RepeatBudgetEntity?

    @Query("DELETE FROM repeat_budgets WHERE id = :id")
    suspend fun deleteById(id: Long)
}