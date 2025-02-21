package ru.crazerr.feature.account.presentation

import ru.crazerr.feature.currency.domain.api.Currency

sealed interface AccountEditorViewAction {
    data object BackClick : AccountEditorViewAction

    data class UpdateCurrentAmount(val amount: String) : AccountEditorViewAction
    data class UpdateName(val name: String) : AccountEditorViewAction

    data object ManageDropdown : AccountEditorViewAction
    data class SelectCurrency(val currency: Currency) : AccountEditorViewAction

    data object SaveClick : AccountEditorViewAction
}