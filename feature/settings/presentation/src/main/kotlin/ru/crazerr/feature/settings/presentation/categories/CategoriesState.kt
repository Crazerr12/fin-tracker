package ru.crazerr.feature.settings.presentation.categories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.crazerr.feature.category.presentation.api.model.CategoryUi

data class CategoriesState(
    val isLoading: Boolean = true,
    val categories: Flow<PagingData<CategoryUi>> = emptyFlow(),
)
