package ru.crazerr.feature.main.presentation.main

import ru.crazerr.feature.account.domain.api.Account

data class MainState(
    val accounts: List<Account>,
    val isLoading: Boolean,
    val currentAmount: Double,
    val currentIncome: Double,
    val currentExpenses: Double,
    val lastMonthExpenses: Double,
    val lastMonthIncome: Double,
    val mainCurrencySign: Char,
    val isDeleteAccountDialogVisible: Boolean,
    val deletableAccount: Account?,
    val deletableAccountIndex: Int,
)

internal val InitialMainState = MainState(
    accounts = emptyList(),
    isLoading = true,
    currentAmount = 0.0,
    currentIncome = 0.0,
    currentExpenses = 0.0,
    lastMonthIncome = 0.0,
    lastMonthExpenses = 0.0,
    mainCurrencySign = '₽',
    isDeleteAccountDialogVisible = false,
    deletableAccount = null,
    deletableAccountIndex = -1,
)
