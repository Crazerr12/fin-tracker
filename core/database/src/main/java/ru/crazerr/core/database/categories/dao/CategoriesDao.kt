package ru.crazerr.core.database.categories.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.categories.model.CategoryEntity
import ru.crazerr.core.database.categories.model.CategoryWithIcon

@Dao
interface CategoriesDao : BaseDao<CategoryEntity> {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryWithIcon>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryWithIcon

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)

    @Query("SELECT * FROM categories LIMIT :limit OFFSET :offset")
    suspend fun getCategoriesByLimitAndOffset(limit: Int, offset: Int): List<CategoryWithIcon>
}