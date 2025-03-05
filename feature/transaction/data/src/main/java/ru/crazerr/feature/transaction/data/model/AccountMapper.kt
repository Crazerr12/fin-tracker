package ru.crazerr.feature.transaction.data.model

import ru.crazerr.core.database.accounts.model.AccountWithCurrency
import ru.crazerr.core.database.currencies.model.CurrencyEntity
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.currency.domain.api.Currency

internal fun AccountWithCurrency.toAccount() = Account(
    id = account.id,
    name = account.name,
    amount = account.amount,
    iconId = account.iconId,
    currency = currency.toCurrency()
)

internal fun CurrencyEntity.toCurrency() = Currency(
    id = id,
    name = name,
    symbol = symbol,
    code = code,
)