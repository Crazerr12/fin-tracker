package ru.crazerr.feature.budget.data.dataSource

import kotlinx.coroutines.flow.first
import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.category.data.api.toCategory

internal class CategoryLocalDataSource(
    private val categoriesDao: CategoriesDao,
) {
    suspend fun getAllCategories() = runCatching {
        categoriesDao.getAllCategories().first().map { it.toCategory() }
    }
}