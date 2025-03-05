package ru.crazerr.feature.transaction.presentation.transactionEditorStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.account.presentation.AccountEditorComponentFactory
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactory
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponentFactory

interface TransactionEditorStoryComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (TransactionEditorStoryComponentAction) -> Unit,
        args: TransactionEditorStoryArgs,
    ): TransactionEditorStoryComponent
}

internal class TransactionEditorStoryComponentFactoryImpl(
    private val accountEditorComponentFactory: AccountEditorComponentFactory,
    private val transactionEditorComponentFactory: TransactionEditorComponentFactory,
    private val categoryEditorComponentFactory: CategoryEditorComponentFactory,
) : TransactionEditorStoryComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (TransactionEditorStoryComponentAction) -> Unit,
        args: TransactionEditorStoryArgs
    ): TransactionEditorStoryComponent = TransactionEditorStoryComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = TransactionEditorStoryDependencies(
            args = args,
            transactionEditorComponentFactory = transactionEditorComponentFactory,
            accountEditorComponentFactory = accountEditorComponentFactory,
            categoryEditorComponentFactory = categoryEditorComponentFactory
        )
    )
}