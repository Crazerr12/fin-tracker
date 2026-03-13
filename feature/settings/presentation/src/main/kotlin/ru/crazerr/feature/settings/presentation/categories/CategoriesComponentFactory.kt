package ru.crazerr.feature.settings.presentation.categories

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.settings.domain.repository.CategoryRepository

interface CategoriesComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (CategoriesComponentAction) -> Unit,
        args: CategoriesArgs
    ): CategoriesComponent
}

internal class CategoriesComponentFactoryImpl(
    private val categoryRepository: CategoryRepository,
) : CategoriesComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (CategoriesComponentAction) -> Unit,
        args: CategoriesArgs
    ): CategoriesComponent = CategoriesComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = CategoriesDependencies(
            args = args,
            categoryRepository = categoryRepository,
        )
    )
}