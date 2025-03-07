package ru.crazerr.feature.transaction.data.model

import ru.crazerr.core.database.transactions.model.TransactionEntity
import ru.crazerr.core.database.transactions.model.TransactionWithAccountAndCategory
import ru.crazerr.feature.account.data.api.toAccount
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType

internal fun TransactionWithAccountAndCategory.toTransaction() = Transaction(
    id = transaction.id,
    category = category.toCategory(),
    amount = transaction.amount,
    type = TransactionType.entries.first { it.id == transaction.type },
    date = transaction.date,
    account = accountWithCurrency.toAccount(),
)

internal fun Transaction.toTransactionEntity() = TransactionEntity(
    categoryId = category.id,
    amount = amount,
    type = type.id,
    date = date,
    accountId = account.id
)