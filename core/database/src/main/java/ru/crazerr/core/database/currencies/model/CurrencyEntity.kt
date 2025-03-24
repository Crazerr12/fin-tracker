package ru.crazerr.core.database.currencies.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val symbol: String,
    val code: String,
)