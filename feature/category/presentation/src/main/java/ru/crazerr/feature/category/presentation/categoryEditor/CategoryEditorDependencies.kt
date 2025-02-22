package ru.crazerr.feature.category.presentation.categoryEditor

import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.category.domain.repository.CategoryRepository

data class CategoryEditorDependencies(
    val args: CategoryEditorArgs,
    val categoryRepository: CategoryRepository,
    val resourceManager: ResourceManager,
)
