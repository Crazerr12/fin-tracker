package ru.crazerr.feature.transaction.data.dataSource

import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.category.data.api.toCategory
import ru.crazerr.feature.domain.api.Category

internal class LocalCategoryDataSource(
    private val categoryDao: CategoriesDao,
) {
    suspend fun getCategories(): Result<List<Category>> =
        try {
            val categoryEntities = categoryDao.getAllCategories()

            Result.success(categoryEntities.map { it.toCategory() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
}