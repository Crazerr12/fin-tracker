package ru.crazerr.feature.account.data.models

import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.accounts.model.AccountWithCurrency
import ru.crazerr.feature.account.domain.api.Account

internal fun AccountWithCurrency.toAccount() = Account(
    id = account.id,
    name = account.name,
    amount = account.amount,
    iconId = account.iconId,
    currency = currency.toCurrency()
)

internal fun Account.toAccountEntity() = AccountEntity(
    id = id,
    name = name,
    amount = amount,
    iconId = iconId,
    currencyId = currency.id,
)
