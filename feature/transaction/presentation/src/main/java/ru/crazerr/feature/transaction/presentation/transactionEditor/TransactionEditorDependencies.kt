package ru.crazerr.feature.transaction.presentation.transactionEditor

import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.transaction.domain.AccountRepository
import ru.crazerr.feature.transaction.domain.CategoryRepository
import ru.crazerr.feature.transaction.domain.TransactionRepository

data class TransactionEditorDependencies(
    val transactionRepository: TransactionRepository,
    val accountRepository: AccountRepository,
    val categoryRepository: CategoryRepository,
    val resourceManager: ResourceManager,
    val args: TransactionEditorArgs,
)
