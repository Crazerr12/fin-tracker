package ru.crazerr.core.database.accounts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    )],
    indices = [Index("currency_id")]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amount: Double,
    @ColumnInfo(name = "icon_id") val iconId: String,
    @ColumnInfo(name = "currency_id") val currencyId: Long,
)
