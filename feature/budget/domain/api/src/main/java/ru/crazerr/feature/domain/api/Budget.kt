package ru.crazerr.feature.domain.api

import java.time.LocalDate

data class Budget(
    val id: Long,
    val category: Category,
    val maxAmount: Double,
    val currentAmount: Double,
    val repeatBudgetId: Long?,
    val date: LocalDate,
    val isRegular: Boolean,
    val isAlarm: Boolean,
    val isWarning: Boolean,
)