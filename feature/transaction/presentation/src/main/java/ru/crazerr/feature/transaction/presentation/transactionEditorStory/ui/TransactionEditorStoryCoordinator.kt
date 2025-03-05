package ru.crazerr.feature.transaction.presentation.transactionEditorStory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.feature.account.presentation.ui.AccountEditorView
import ru.crazerr.feature.category.presentation.categoryEditor.ui.CategoryEditorView
import ru.crazerr.feature.transaction.presentation.transactionEditor.ui.TransactionEditorView
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryComponent

@Composable
fun TransactionEditorStoryCoordinator(
    modifier: Modifier = Modifier,
    component: TransactionEditorStoryComponent
) {
    val stack by component.childStack.subscribeAsState()

    Children(stack = stack, modifier = modifier) {
        when (val child = it.instance) {
            is TransactionEditorStoryComponent.Child.AccountEditor -> AccountEditorView(component = child.component)
            is TransactionEditorStoryComponent.Child.CategoryEditor -> CategoryEditorView(component = child.component)
            is TransactionEditorStoryComponent.Child.TransactionEditor -> TransactionEditorView(
                component = child.component
            )
        }
    }
}