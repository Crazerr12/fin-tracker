package ru.crazerr.feature.transaction.domain.repository

import ru.crazerr.feature.category.domain.api.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}