package ru.crazerr.core.database.accounts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.crazerr.core.database.currencies.model.CurrencyEntity

@Entity(
    tableName = "accounts",
    foreignKeys = [ForeignKey(
        entity = CurrencyEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("currency_id"),
        onUpdate = ForeignKey.RESTRICT,
        onDelete = ForeignKey.RESTRICT,
    )]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Long,
    @ColumnInfo(name = "icon_id") val iconId: String,
    @ColumnInfo(name = "currency_id") val currencyId: Int,
)
