package ru.crazerr.feature.transactions.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.domain.api.Category

interface CategoryRepository {
    fun getCategories(): Result<Flow<List<Category>>>
}