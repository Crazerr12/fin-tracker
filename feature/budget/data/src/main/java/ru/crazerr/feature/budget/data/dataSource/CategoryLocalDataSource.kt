package ru.crazerr.feature.budget.data.dataSource

import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.category.data.api.toCategory

internal class CategoryLocalDataSource(
    private val categoriesDao: CategoriesDao,
) {
    suspend fun getAllCategories() = runCatching {
        categoriesDao.getAllCategories().map { it.toCategory() }
    }
}