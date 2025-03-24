package ru.crazerr.core.database.budgets.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.crazerr.core.database.categories.model.CategoryEntity
import java.time.LocalDate

@Entity(
    tableName = "budgets",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
    ), ForeignKey(
        entity = RepeatBudgetEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("repeat_budget_id"),
        onDelete = ForeignKey.SET_NULL,
    )],
    indices = [Index("repeat_budget_id"), Index("category_id", "date")]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "max_amount") val maxAmount: Double,
    @ColumnInfo(name = "current_amount") val currentAmount: Double,
    @ColumnInfo(name = "repeat_budget_id") val repeatBudgetId: Long?,
    val date: LocalDate,
    @ColumnInfo(name = "is_alarm") val isAlarm: Boolean,
    @ColumnInfo(name = "is_warning") val isWarning: Boolean,
)
