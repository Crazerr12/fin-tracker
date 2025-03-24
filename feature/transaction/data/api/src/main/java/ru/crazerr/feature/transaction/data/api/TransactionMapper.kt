package ru.crazerr.feature.transaction.data.api

import ru.crazerr.core.database.transactions.model.TransactionEntity
import ru.crazerr.core.database.transactions.model.TransactionWithAccountAndCategory
import ru.crazerr.feature.account.data.api.toAccount
import ru.crazerr.feature.category.data.api.toCategory
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType


fun TransactionWithAccountAndCategory.toTransaction() = Transaction(
    id = transaction.id,
    category = category.toCategory(),
    amount = transaction.amount,
    type = TransactionType.fromId(transaction.type)!!,
    date = transaction.date,
    account = accountWithCurrency.toAccount(),
)

fun Transaction.toTransactionEntity() = TransactionEntity(
    id = if (id == -1L) 0 else id,
    categoryId = this.category.id,
    amount = amount,
    type = type.id,
    date = date,
    accountId = account.id
)