package ru.crazerr.core.database.accounts.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.crazerr.core.database.currencies.model.CurrencyEntity
import ru.crazerr.core.database.icons.model.IconEntity

data class AccountWithCurrencyAndIcon(
    @Embedded val account: AccountEntity,
    @Relation(entity = CurrencyEntity::class, parentColumn = "currency_id", entityColumn = "id")
    val currency: CurrencyEntity,
    @Relation(entity = IconEntity::class, parentColumn = "icon_id", entityColumn = "id")
    val icon: IconEntity,
)