package ru.crazerr.feature.account.presentation

import ru.crazerr.feature.currency.domain.api.Currency
import ru.crazerr.feature.icon.domain.api.IconModel

data class AccountEditorState(
    val id: Long,
    val name: String,
    val nameError: String,
    val amount: String,
    val amountError: String,
    val selectedCurrency: Currency,
    val isDropdownExpanded: Boolean,
    val currencies: List<Currency>,
    val isLoading: Boolean,
    val buttonIsLoading: Boolean,
    val icons: List<IconModel>,
    val selectedIcon: IconModel,
)

internal val InitialAccountEditorState = AccountEditorState(
    id = -1,
    name = "",
    nameError = "",
    amount = "",
    amountError = "",
    selectedCurrency = Currency(id = 0, name = "", symbol = "", code = ""),
    currencies = emptyList(),
    isDropdownExpanded = false,
    isLoading = false,
    buttonIsLoading = false,
    icons = emptyList(),
    selectedIcon = IconModel(id = -1, icon = ByteArray(0), purpose = ""),
)
