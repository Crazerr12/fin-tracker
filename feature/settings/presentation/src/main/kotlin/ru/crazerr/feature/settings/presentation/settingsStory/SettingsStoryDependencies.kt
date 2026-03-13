package ru.crazerr.feature.settings.presentation.settingsStory

import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactory
import ru.crazerr.feature.settings.presentation.categories.CategoriesComponentFactory
import ru.crazerr.feature.settings.presentation.settings.SettingsComponentFactory

class SettingsStoryDependencies(
    val settingsComponentFactory: SettingsComponentFactory,
    val categoriesComponentFactory: CategoriesComponentFactory,
    val categoryEditorComponentFactory: CategoryEditorComponentFactory,
)