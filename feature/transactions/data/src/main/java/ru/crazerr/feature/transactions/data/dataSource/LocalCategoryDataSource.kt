package ru.crazerr.feature.transactions.data.dataSource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.category.data.api.toCategory
import ru.crazerr.feature.domain.api.Category

internal class LocalCategoryDataSource(
    private val categoriesDao: CategoriesDao,
) {
    fun getAllCategories(): Result<Flow<List<Category>>> = runCatching {
        categoriesDao.getAllCategories().map { it.map { it.toCategory() } }
    }
}