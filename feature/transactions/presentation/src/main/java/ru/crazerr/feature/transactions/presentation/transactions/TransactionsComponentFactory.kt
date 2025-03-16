package ru.crazerr.feature.transactions.presentation.transactions

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.transactions.domain.repository.AccountRepository
import ru.crazerr.feature.transactions.domain.repository.CategoryRepository
import ru.crazerr.feature.transactions.domain.repository.TransactionRepository

interface TransactionsComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (TransactionsComponentAction) -> Unit,
        args: TransactionsArgs,
    ): TransactionsComponent
}

internal class TransactionsComponentFactoryImpl(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
) : TransactionsComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (TransactionsComponentAction) -> Unit,
        args: TransactionsArgs,
    ): TransactionsComponent = TransactionsComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = TransactionsDependencies(
            accountRepository = accountRepository,
            categoryRepository = categoryRepository,
            transactionRepository = transactionRepository,
            args = args,
        )
    )
}