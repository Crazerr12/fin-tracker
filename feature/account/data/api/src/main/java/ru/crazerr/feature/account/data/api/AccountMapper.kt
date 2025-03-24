package ru.crazerr.feature.account.data.api

import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.accounts.model.AccountWithCurrency
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.currency.data.api.toCurrency


fun AccountWithCurrency.toAccount() = Account(
    id = account.id,
    name = account.name,
    amount = account.amount,
    iconId = account.iconId,
    currency = currency.toCurrency()
)

fun Account.toAccountEntity() = AccountEntity(
    id = if (id == -1L) 0 else id,
    name = name,
    amount = amount,
    iconId = iconId,
    currencyId = currency.id,
)
