package ru.crazerr.feature.transactions.data.dataSource

import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.category.data.api.toCategory
import ru.crazerr.feature.domain.api.Category

internal class LocalCategoryDataSource(
    private val categoriesDao: CategoriesDao,
) {
    suspend fun getAllCategories(): Result<List<Category>> = try {
        val categories = categoriesDao.getAllCategories()

        Result.success(categories.map { it.toCategory() })
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}