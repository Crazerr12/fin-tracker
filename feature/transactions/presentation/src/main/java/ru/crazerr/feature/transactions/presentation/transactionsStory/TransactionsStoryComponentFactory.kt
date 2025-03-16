package ru.crazerr.feature.transactions.presentation.transactionsStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryComponentFactory
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponentFactory
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponentFactory

interface TransactionsStoryComponentFactory {
    fun create(componentContext: ComponentContext): TransactionsStoryComponent
}

internal class TransactionsStoryComponentFactoryImpl(
    private val transactionsComponentFactory: TransactionsComponentFactory,
    private val transactionsFilterComponentFactory: TransactionsFilterComponentFactory,
    private val transactionEditorStoryComponentFactory: TransactionEditorStoryComponentFactory,
) : TransactionsStoryComponentFactory {
    override fun create(componentContext: ComponentContext): TransactionsStoryComponent =
        TransactionsStoryComponent(
            componentContext = componentContext,
            dependencies = TransactionsStoryDependencies(
                transactionsComponentFactory = transactionsComponentFactory,
                transactionsFilterComponentFactory = transactionsFilterComponentFactory,
                transactionEditorStoryComponentFactory = transactionEditorStoryComponentFactory,
            )
        )
}