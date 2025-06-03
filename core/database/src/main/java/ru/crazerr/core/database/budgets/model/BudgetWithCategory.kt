package ru.crazerr.core.database.budgets.model

import androidx.room.ColumnInfo
import java.time.LocalDate

data class BudgetWithCategory(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "max_amount") val maxAmount: Double,
    @ColumnInfo(name = "current_amount") val currentAmount: Double,
    @ColumnInfo(name = "repeat_budget_id") val repeatBudgetId: Long?,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "is_alarm") val isAlarm: Boolean,
    @ColumnInfo(name = "is_warning") val isWarning: Boolean,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "category_color") val categoryColor: Long,
    @ColumnInfo(name = "icon_id") val iconId: Long,
    @ColumnInfo(name = "category_is_template") val categoryIsTemplate: Boolean,
    @ColumnInfo(name = "icon") val icon: ByteArray,
    @ColumnInfo(name = "icon_purpose") val iconPurpose: String,
)