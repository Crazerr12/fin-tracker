package ru.crazerr.feature.main.domain.models

data class IncomeAndExpenses(
    val currentIncome: Long,
    val lastMonthIncome: Long,
    val currentExpenses: Long,
    val lastMonthExpenses: Long,
)