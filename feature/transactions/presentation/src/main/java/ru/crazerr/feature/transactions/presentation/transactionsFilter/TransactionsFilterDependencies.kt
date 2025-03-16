package ru.crazerr.feature.transactions.presentation.transactionsFilter

import ru.crazerr.feature.transactions.domain.repository.AccountRepository
import ru.crazerr.feature.transactions.domain.repository.CategoryRepository

class TransactionsFilterDependencies(
    val accountRepository: AccountRepository,
    val categoryRepository: CategoryRepository,
    val args: TransactionsFilterArgs,
)