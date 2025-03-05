package ru.crazerr.feature.transaction.data.repository

import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transaction.data.dataSource.LocalCategoryDataSource
import ru.crazerr.feature.transaction.domain.CategoryRepository

internal class CategoryRepositoryImpl(
    private val localCategoryDataSource: LocalCategoryDataSource,
) : CategoryRepository {
    override suspend fun getCategories(): Result<List<Category>> {
        return localCategoryDataSource.getCategories()
    }
}