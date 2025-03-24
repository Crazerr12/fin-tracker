package ru.crazerr.feature.transaction.data.repository

import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transaction.data.dataSource.CategoryLocalDataSource
import ru.crazerr.feature.transaction.domain.repository.CategoryRepository

internal class CategoryRepositoryImpl(
    private val categoryLocalDataSource: CategoryLocalDataSource,
) : CategoryRepository {
    override suspend fun getCategories(): Result<List<Category>> {
        return categoryLocalDataSource.getCategories()
    }
}