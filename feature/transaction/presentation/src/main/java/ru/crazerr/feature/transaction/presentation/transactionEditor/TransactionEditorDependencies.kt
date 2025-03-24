package ru.crazerr.feature.transaction.presentation.transactionEditor

import ru.crazerr.core.utils.notifications.NotificationSender
import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.transaction.domain.repository.AccountRepository
import ru.crazerr.feature.transaction.domain.repository.CategoryRepository
import ru.crazerr.feature.transaction.domain.repository.TransactionRepository

data class TransactionEditorDependencies(
    val transactionRepository: TransactionRepository,
    val accountRepository: AccountRepository,
    val categoryRepository: CategoryRepository,
    val resourceManager: ResourceManager,
    val notificationSender: NotificationSender,
    val args: TransactionEditorArgs,
)
