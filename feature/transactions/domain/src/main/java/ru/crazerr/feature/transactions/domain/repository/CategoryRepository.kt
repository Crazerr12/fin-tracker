package ru.crazerr.feature.transactions.domain.repository

import ru.crazerr.feature.domain.api.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}