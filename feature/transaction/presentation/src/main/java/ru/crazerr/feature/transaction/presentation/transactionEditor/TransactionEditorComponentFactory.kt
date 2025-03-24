package ru.crazerr.feature.transaction.presentation.transactionEditor

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.notifications.NotificationSender
import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.transaction.domain.repository.AccountRepository
import ru.crazerr.feature.transaction.domain.repository.CategoryRepository
import ru.crazerr.feature.transaction.domain.repository.TransactionRepository

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
    private val resourceManager: ResourceManager,
    private val notificationSender: NotificationSender,
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
            resourceManager = resourceManager,
            notificationSender = notificationSender,
        )
    )
}