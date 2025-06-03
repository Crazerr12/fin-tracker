package ru.crazerr.feature.transactions.data.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transactions.data.dataSource.LocalCategoryDataSource
import ru.crazerr.feature.transactions.domain.repository.CategoryRepository

internal class CategoryRepositoryImpl(
    private val localCategoryDataSource: LocalCategoryDataSource,
) : CategoryRepository {
    override fun getCategories(): Result<Flow<List<Category>>> =
        localCategoryDataSource.getAllCategories()
}