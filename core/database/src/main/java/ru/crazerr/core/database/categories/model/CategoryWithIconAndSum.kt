package ru.crazerr.core.database.categories.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class CategoryWithIconAndSum(
    @Embedded val categoryWithIcon: CategoryWithIcon,
    @ColumnInfo("total_sum") val totalSum: Double,
)