package ru.crazerr.feature.account.presentation

import ru.crazerr.feature.currency.domain.api.Currency

data class AccountEditorState(
    val id: Int,
    val name: String,
    val nameError: String,
    val amount: String,
    val amountError: String,
    val selectedCurrency: Currency,
    val isDropdownExpanded: Boolean,
    val currencies: List<Currency>,
    val isLoading: Boolean,
    val buttonIsLoading: Boolean,
)

internal val InitialAccountEditorState = AccountEditorState(
    id = 0,
    name = "",
    nameError = "",
    amount = "",
    amountError = "",
    selectedCurrency = Currency(id = 0, name = "", symbol = "", code = ""),
    currencies = emptyList(),
    isDropdownExpanded = false,
    isLoading = false,
    buttonIsLoading = false,
)
