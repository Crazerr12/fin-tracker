package ru.crazerr.core.database.accounts.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.crazerr.core.database.currencies.model.CurrencyEntity

data class AccountWithCurrency(
    @Embedded val account: AccountEntity,
    @Relation(entity = CurrencyEntity::class, parentColumn = "currency_id", entityColumn = "id")
    val currency: CurrencyEntity,
)