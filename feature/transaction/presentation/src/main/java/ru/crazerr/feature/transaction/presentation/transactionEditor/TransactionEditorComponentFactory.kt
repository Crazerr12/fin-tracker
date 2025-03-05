package ru.crazerr.feature.transaction.presentation.transactionEditor

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.transaction.domain.AccountRepository
import ru.crazerr.feature.transaction.domain.CategoryRepository
import ru.crazerr.feature.transaction.domain.TransactionRepository

interface TransactionEditorComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (TransactionEditorComponentAction) -> Unit,
        args: TransactionEditorArgs
    ): TransactionEditorComponent
}

internal class TransactionEditorComponentFactoryImpl(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
) : TransactionEditorComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (TransactionEditorComponentAction) -> Unit,
        args: TransactionEditorArgs
    ): TransactionEditorComponent = TransactionEditorComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = TransactionEditorDependencies(
            transactionRepository = transactionRepository,
            categoryRepository = categoryRepository,
            accountRepository = accountRepository,
            args = args,
        )
    )
}