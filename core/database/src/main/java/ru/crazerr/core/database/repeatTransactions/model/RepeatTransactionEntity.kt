package ru.crazerr.core.database.repeatTransactions.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.categories.model.CategoryEntity
import java.time.LocalDate

@Entity(
    tableName = "repeat_transactions",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("account_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )],
    indices = [Index("category_id", "account_id")]
)
data class RepeatTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    val amount: Double,
    val type: Boolean,
    @ColumnInfo(name = "completion_date") val completionDate: LocalDate,
    @ColumnInfo(name = "account_id") val accountId: Long,
    @ColumnInfo(name = "repeat_type") val repeatType: Int,
    @ColumnInfo(name = "repeat_interval") val repeatInterval: Int,
    @ColumnInfo(name = "repeat_units") val repeatUnits: List<Int>,
)