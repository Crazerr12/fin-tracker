package ru.crazerr.feature.analysis.presentation.analysisStory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.feature.analysis.presentation.analysis.ui.AnalysisView
import ru.crazerr.feature.analysis.presentation.analysisStory.AnalysisStoryComponent

@Composable
fun AnalysisStoryCoordinator(modifier: Modifier = Modifier, component: AnalysisStoryComponent) {
    val stack by component.stack.subscribeAsState()

    Children(stack = stack, modifier = modifier) {
        when (val child = it.instance) {
            is AnalysisStoryComponent.Child.Analysis -> AnalysisView(component = child.component)
        }
    }
}