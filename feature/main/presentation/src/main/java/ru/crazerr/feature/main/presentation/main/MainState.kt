package ru.crazerr.feature.main.presentation.main

import ru.crazerr.feature.account.domain.api.Account

data class MainState(
    val accounts: List<Account>,
    val isLoading: Boolean,
    val currentAmount: Long,
    val currentIncome: Long,
    val currentExpenses: Long,
    val lastMonthExpenses: Long,
    val lastMonthIncome: Long,
    val mainCurrencySign: Char,
)

internal val InitialMainState = MainState(
    accounts = emptyList(),
    isLoading = true,
    currentAmount = 0L,
    currentIncome = 0L,
    currentExpenses = 0L,
    lastMonthIncome = 0L,
    lastMonthExpenses = 0L,
    mainCurrencySign = '₽',
)
