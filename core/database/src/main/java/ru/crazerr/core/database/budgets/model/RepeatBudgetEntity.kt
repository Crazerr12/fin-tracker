package ru.crazerr.core.database.budgets.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.crazerr.core.database.categories.model.CategoryEntity

@Entity(
    tableName = "repeat_budgets",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id")
    )],
    indices = [Index("category_id")]
)
data class RepeatBudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "max_amount") val maxAmount: Long,
    @ColumnInfo(name = "is_alarm") val isAlarm: Boolean,
    @ColumnInfo(name = "is_warning") val isWarning: Boolean,
)
