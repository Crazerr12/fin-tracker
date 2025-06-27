package ru.crazerr.feature.main.presentation.main

import ru.crazerr.feature.account.domain.api.Account

sealed interface MainViewAction {
    data class ShowDeleteAccountDialog(val account: Account, val index: Int) : MainViewAction
    data object CancelDeleteAccount : MainViewAction
    data object DeleteAccount : MainViewAction
    data class GoToAccount(val id: Long = -1) : MainViewAction
}