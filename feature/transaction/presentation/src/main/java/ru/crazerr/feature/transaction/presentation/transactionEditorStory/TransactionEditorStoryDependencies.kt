package ru.crazerr.feature.transaction.presentation.transactionEditorStory

import ru.crazerr.feature.account.presentation.AccountEditorComponentFactory
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactory
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponentFactory

data class TransactionEditorStoryDependencies(
    val args: TransactionEditorStoryArgs,
    val transactionEditorComponentFactory: TransactionEditorComponentFactory,
    val accountEditorComponentFactory: AccountEditorComponentFactory,
    val categoryEditorComponentFactory: CategoryEditorComponentFactory,
)
