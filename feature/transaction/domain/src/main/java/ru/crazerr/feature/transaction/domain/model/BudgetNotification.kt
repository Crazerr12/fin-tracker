package ru.crazerr.feature.transaction.domain.model

data class BudgetNotification(
    val isWarning: Boolean,
    val isAlarm: Boolean,
    val percentage: Double,
)