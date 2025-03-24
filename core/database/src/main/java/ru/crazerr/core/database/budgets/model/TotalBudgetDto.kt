package ru.crazerr.core.database.budgets.model

import androidx.room.ColumnInfo

data class TotalBudgetDto(
    @ColumnInfo(name = "total") val total: Double,
    @ColumnInfo(name = "current") val current: Double,
)
