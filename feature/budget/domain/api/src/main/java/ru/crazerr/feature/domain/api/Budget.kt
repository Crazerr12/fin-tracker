package ru.crazerr.feature.domain.api

import java.time.LocalDate

data class Budget(
    val id: Int,
    val category: Category,
    val maxAmount: Long,
    val currentAmount: Long,
    val repeatBudgetId: Int?,
    val date: LocalDate,
    val isAlarm: Boolean,
    val isWarning: Boolean,
)