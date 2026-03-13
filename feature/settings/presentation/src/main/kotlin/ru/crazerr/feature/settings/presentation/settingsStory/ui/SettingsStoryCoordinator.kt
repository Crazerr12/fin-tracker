package ru.crazerr.feature.settings.presentation.settingsStory.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.crazerr.feature.category.presentation.categoryEditor.ui.CategoryEditorView
import ru.crazerr.feature.settings.presentation.categories.ui.CategoriesView
import ru.crazerr.feature.settings.presentation.settings.ui.SettingsView
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponent
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponent.Child

@Composable
fun SettingsStoryCoordinator(modifier: Modifier = Modifier, component: SettingsStoryComponent) {
    Children(modifier = modifier, stack = component.stack) {
        when (val child = it.instance) {
            is Child.Settings -> SettingsView(component = child.component)
            is Child.Categories -> CategoriesView(component = child.component)
            is Child.CategoryEditor -> CategoryEditorView(component = child.component)
        }
    }
}