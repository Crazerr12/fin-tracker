package ru.crazerr.feature.budget.presentation.budgetStory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.feature.budget.presentation.budgetEditor.ui.BudgetEditorView
import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryComponent
import ru.crazerr.feature.category.presentation.categoryEditor.ui.CategoryEditorView

@Composable
fun BudgetStoryCoordinator(modifier: Modifier = Modifier, component: BudgetStoryComponent) {
    val stack by component.stack.subscribeAsState()

    Children(modifier = modifier, stack = stack) {
        when (val child = it.instance) {
            is BudgetStoryComponent.Child.BudgetEditor -> BudgetEditorView(component = child.component)
            is BudgetStoryComponent.Child.CategoryEditor -> CategoryEditorView(component = child.component)
        }
    }
}