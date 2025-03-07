package ru.crazerr.feature.main.presentation.mainStory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.feature.account.presentation.ui.AccountEditorView
import ru.crazerr.feature.main.presentation.main.ui.MainView
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponent

@Composable
fun MainStoryCoordinator(modifier: Modifier = Modifier, component: MainStoryComponent) {
    val stack by component.stack.subscribeAsState()

    Children(stack = stack, modifier = modifier) {
        when (val child = it.instance) {
            is MainStoryComponent.Child.AccountEditor -> AccountEditorView(component = child.component)
            is MainStoryComponent.Child.Main -> MainView(component = child.component)
        }
    }
}