package ru.crazerr.core.database.categories.dao

import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.categories.model.CategoryEntity

interface CategoriesDao: BaseDao<CategoryEntity> {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<CategoryEntity>
}