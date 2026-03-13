package ru.crazerr.feature.settings.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.category.domain.api.Category
import ru.crazerr.feature.settings.data.dataSource.LocalCategoryDataSource
import ru.crazerr.feature.settings.data.pagingSource.CategoryPagingSource
import ru.crazerr.feature.settings.domain.repository.CategoryRepository

internal class CategoryRepositoryImpl(
    private val localCategoryDataSource: LocalCategoryDataSource,
) : CategoryRepository {
    override fun getCategories(): Flow<PagingData<Category>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                CategoryPagingSource(localCategoryDataSource = localCategoryDataSource)
            }
        ).flow
    }

    override suspend fun deleteCategoryById(categoryId: Long): Result<Long> =
        localCategoryDataSource.deleteCategory(categoryId = categoryId)
}