package ru.crazerr.feature.main.domain.models

data class IncomeAndExpenses(
    val currentIncome: Double,
    val lastMonthIncome: Double,
    val currentExpenses: Double,
    val lastMonthExpenses: Double,
)