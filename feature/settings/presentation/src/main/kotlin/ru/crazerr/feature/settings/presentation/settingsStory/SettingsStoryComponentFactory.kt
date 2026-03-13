package ru.crazerr.feature.settings.presentation.settingsStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.presentation.ComponentFactory
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactory
import ru.crazerr.feature.settings.presentation.categories.CategoriesComponentFactory
import ru.crazerr.feature.settings.presentation.settings.SettingsComponentFactory

interface SettingsStoryComponentFactory :
    ComponentFactory<SettingsStoryComponent, SettingsStoryComponentAction>

internal class SettingsStoryComponentFactoryImpl(
    private val settingsComponentFactory: SettingsComponentFactory,
    private val categoriesComponentFactory: CategoriesComponentFactory,
    private val categoryEditorComponentFactory: CategoryEditorComponentFactory,
) : SettingsStoryComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (SettingsStoryComponentAction) -> Unit
    ): SettingsStoryComponent = SettingsStoryComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = SettingsStoryDependencies(
            settingsComponentFactory = settingsComponentFactory,
            categoriesComponentFactory = categoriesComponentFactory,
            categoryEditorComponentFactory = categoryEditorComponentFactory,
        ),
    )
}