package ru.crazerr.feature.transaction.data.dataSource

import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transaction.data.model.toCategory

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