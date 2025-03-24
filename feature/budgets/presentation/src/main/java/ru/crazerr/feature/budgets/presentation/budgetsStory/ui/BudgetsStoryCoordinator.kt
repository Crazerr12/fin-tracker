package ru.crazerr.feature.budgets.presentation.budgetsStory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.feature.budget.presentation.budgetStory.ui.BudgetStoryCoordinator
import ru.crazerr.feature.budgets.presentation.budgets.ui.BudgetsView
import ru.crazerr.feature.budgets.presentation.budgetsStory.BudgetsStoryComponent

@Composable
fun BudgetsStoryCoordinator(modifier: Modifier = Modifier, component: BudgetsStoryComponent) {
    val stack by component.stack.subscribeAsState()

    Children(stack = stack, modifier = modifier) {
        when (val child = it.instance) {
            is BudgetsStoryComponent.Child.BudgetStory -> BudgetStoryCoordinator(component = child.component)
            is BudgetsStoryComponent.Child.Budgets -> BudgetsView(component = child.component)
        }
    }
}