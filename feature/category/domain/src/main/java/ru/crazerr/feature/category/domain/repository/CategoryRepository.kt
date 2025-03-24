package ru.crazerr.feature.category.domain.repository

import ru.crazerr.feature.domain.api.Category

interface CategoryRepository {
    suspend fun getCategoryById(id: Long): Result<Category>

    suspend fun createCategory(category: Category): Result<Category>

    suspend fun updateCategory(category: Category): Result<Category>
}