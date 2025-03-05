package ru.crazerr.core.database.categories.dao

import androidx.room.Dao
import androidx.room.Query
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.categories.model.CategoryEntity

@Dao
interface CategoriesDao: BaseDao<CategoryEntity> {
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity
}