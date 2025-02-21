package ru.crazerr.feature.account.presentation

import ru.crazerr.feature.account.domain.api.Account

sealed interface AccountEditorComponentAction {
    data object BackClick : AccountEditorComponentAction
    data class SaveAccount(val account: Account) : AccountEditorComponentAction
}