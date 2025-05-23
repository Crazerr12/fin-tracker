package ru.crazerr.feature.transactions.presentation.transactionsStory

import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryComponentFactory
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponentFactory
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponentFactory

class TransactionsStoryDependencies(
    val args: TransactionsStoryArgs,
    val transactionsComponentFactory: TransactionsComponentFactory,
    val transactionsFilterComponentFactory: TransactionsFilterComponentFactory,
    val transactionEditorStoryComponentFactory: TransactionEditorStoryComponentFactory,
)