package ru.crazerr.feature.budget.domain

import ru.crazerr.feature.category.domain.api.Category

interface CategoryRepository {
    suspend fun getAllCategories(): Result<List<Category>>
}