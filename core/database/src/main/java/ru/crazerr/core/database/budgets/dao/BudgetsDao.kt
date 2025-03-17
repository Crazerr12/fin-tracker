package ru.crazerr.core.database.budgets.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.budgets.model.BudgetEntity
import ru.crazerr.core.database.budgets.model.BudgetWithCategory

@Dao
interface BudgetsDao : BaseDao<BudgetEntity> {

    @Query("SELECT * FROM budgets ")
    fun getAllBudgets(): Flow<BudgetWithCategory>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetWithCategory
}