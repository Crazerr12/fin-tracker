package ru.crazerr.feature.category.presentation.categoryEditor

import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.category.domain.repository.CategoryRepository
import ru.crazerr.feature.category.domain.repository.IconRepository

data class CategoryEditorDependencies(
    val args: CategoryEditorArgs,
    val iconRepository: IconRepository,
    val categoryRepository: CategoryRepository,
    val resourceManager: ResourceManager,
)
