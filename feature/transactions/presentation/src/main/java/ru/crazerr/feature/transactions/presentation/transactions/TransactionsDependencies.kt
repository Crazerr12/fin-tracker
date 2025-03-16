package ru.crazerr.feature.transactions.presentation.transactions

import ru.crazerr.feature.transactions.domain.repository.AccountRepository
import ru.crazerr.feature.transactions.domain.repository.CategoryRepository
import ru.crazerr.feature.transactions.domain.repository.TransactionRepository

class TransactionsDependencies(
    val accountRepository: AccountRepository,
    val transactionRepository: TransactionRepository,
    val categoryRepository: CategoryRepository,
    val args: TransactionsArgs,
)