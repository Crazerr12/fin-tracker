package ru.crazerr.feature.settings.data.dataSource

import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.category.data.api.toCategory
import ru.crazerr.feature.category.domain.api.Category

internal class LocalCategoryDataSource(
    private val categoriesDao: CategoriesDao,
) {
    suspend fun getCategories(limit: Int, offset: Int): Result<List<Category>> = runCatching {
        categoriesDao.getCategoriesByLimitAndOffset(limit = limit, offset = offset)
            .map { it.toCategory() }
    }

    suspend fun deleteCategory(categoryId: Long): Result<Long> = runCatching {
        categoriesDao.deleteCategoryById(id = categoryId)
        return Result.success(categoryId)
    }
}