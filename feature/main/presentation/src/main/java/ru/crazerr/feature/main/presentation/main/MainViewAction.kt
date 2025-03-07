package ru.crazerr.feature.main.presentation.main

import ru.crazerr.feature.account.domain.api.Account

sealed interface MainViewAction {
    data class DeleteAccount(val account: Account, val index: Int) : MainViewAction
    data class GoToAccount(val id: Int = -1) : MainViewAction
}