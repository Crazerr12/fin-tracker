package ru.crazerr.feature.category.data.repository

import ru.crazerr.feature.category.data.dataSource.CategoryLocalDataSource
import ru.crazerr.feature.category.domain.api.Category
import ru.crazerr.feature.category.domain.repository.CategoryRepository

internal class CategoryRepositoryImpl(
    private val categoryLocalDataSource: CategoryLocalDataSource,
) : CategoryRepository {
    override suspend fun getCategoryById(id: Long): Result<Category> {
        return categoryLocalDataSource.getCategoryById(id = id)
    }

    override suspend fun createCategory(category: Category): Result<Category> {
        return categoryLocalDataSource.createCategory(category = category)
    }

    override suspend fun updateCategory(category: Category): Result<Category> {
        return categoryLocalDataSource.updateCategory(category = category)
    }
}