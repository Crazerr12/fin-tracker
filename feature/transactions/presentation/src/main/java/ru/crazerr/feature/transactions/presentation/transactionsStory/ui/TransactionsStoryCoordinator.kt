package ru.crazerr.feature.transactions.presentation.transactionsStory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.ui.TransactionEditorStoryCoordinator
import ru.crazerr.feature.transactions.presentation.transactions.ui.TransactionsView
import ru.crazerr.feature.transactions.presentation.transactionsFilter.ui.TransactionsFilterView
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryComponent

@Composable
fun TransactionsStoryCoordinator(
    modifier: Modifier = Modifier,
    component: TransactionsStoryComponent
) {
    val stack by component.stack.subscribeAsState()
    Children(modifier = modifier, stack = stack) {
        when (val child = it.instance) {
            is TransactionsStoryComponent.Child.Transactions -> TransactionsView(component = child.component)
            is TransactionsStoryComponent.Child.TransactionsFilter -> TransactionsFilterView(
                component = child.component
            )

            is TransactionsStoryComponent.Child.TransactionEditorStory -> TransactionEditorStoryCoordinator(
                component = child.component
            )
        }
    }
}