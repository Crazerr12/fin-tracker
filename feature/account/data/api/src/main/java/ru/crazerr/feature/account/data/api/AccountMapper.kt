package ru.crazerr.feature.account.data.api

import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.accounts.model.AccountWithCurrencyAndIcon
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.currency.data.api.toCurrency
import ru.crazerr.feature.icon.data.api.toIcon


fun AccountWithCurrencyAndIcon.toAccount() = Account(
    id = account.id,
    name = account.name,
    amount = account.amount,
    icon = icon.toIcon(),
    currency = currency.toCurrency()
)

fun Account.toAccountEntity() = AccountEntity(
    id = if (id == -1L) 0 else id,
    name = name,
    amount = amount,
    iconId = icon.id,
    currencyId = currency.id,
)
