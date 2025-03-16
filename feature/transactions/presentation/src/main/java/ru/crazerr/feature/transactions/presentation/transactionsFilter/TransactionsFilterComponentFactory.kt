package ru.crazerr.feature.transactions.presentation.transactionsFilter

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.transactions.domain.repository.AccountRepository
import ru.crazerr.feature.transactions.domain.repository.CategoryRepository

interface TransactionsFilterComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (TransactionsFilterComponentAction) -> Unit,
        args: TransactionsFilterArgs
    ): TransactionsFilterComponent
}

internal class TransactionsFilterComponentFactoryImpl(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
) : TransactionsFilterComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (TransactionsFilterComponentAction) -> Unit,
        args: TransactionsFilterArgs
    ): TransactionsFilterComponent = TransactionsFilterComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = TransactionsFilterDependencies(
            accountRepository = accountRepository,
            categoryRepository = categoryRepository,
            args = args,
        )
    )
}