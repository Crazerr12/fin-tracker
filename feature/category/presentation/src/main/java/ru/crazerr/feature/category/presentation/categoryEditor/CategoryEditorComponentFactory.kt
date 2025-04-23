package ru.crazerr.feature.category.presentation.categoryEditor

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.category.domain.repository.CategoryRepository
import ru.crazerr.feature.category.domain.repository.IconRepository

interface CategoryEditorComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (CategoryEditorComponentAction) -> Unit,
        args: CategoryEditorArgs
    ): CategoryEditorComponent
}

internal class CategoryEditorComponentFactoryImpl(
    private val resourceManager: ResourceManager,
    private val categoryRepository: CategoryRepository,
    private val iconRepository: IconRepository,
) : CategoryEditorComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (CategoryEditorComponentAction) -> Unit,
        args: CategoryEditorArgs
    ): CategoryEditorComponent = CategoryEditorComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = CategoryEditorDependencies(
            args = args,
            categoryRepository = categoryRepository,
            resourceManager = resourceManager,
            iconRepository = iconRepository,
        )
    )
}