package ru.crazerr.feature.budget.data.repository

import ru.crazerr.feature.budget.data.dataSource.CategoryLocalDataSource
import ru.crazerr.feature.budget.domain.CategoryRepository
import ru.crazerr.feature.domain.api.Category

internal class CategoryRepositoryImpl(
    private val categoryLocalDataSource: CategoryLocalDataSource,
) : CategoryRepository {
    override suspend fun getAllCategories(): Result<List<Category>> =
        categoryLocalDataSource.getAllCategories()
}