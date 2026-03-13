package ru.crazerr.feature.settings.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.category.domain.api.Category

interface CategoryRepository {
    fun getCategories(): Flow<PagingData<Category>>

    suspend fun deleteCategoryById(categoryId: Long): Result<Long>
}