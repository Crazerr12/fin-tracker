package ru.crazerr.feature.account.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.presentation.isValidAmount
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.currency.domain.api.Currency

abstract class AccountEditorComponent :
    BaseComponent<AccountEditorState, AccountEditorViewAction>(InitialAccountEditorState)

internal class AccountEditorComponentImpl(
    componentContext: ComponentContext,
    private val dependencies: AccountEditorDependencies,
    private val onAction: (AccountEditorComponentAction) -> Unit,
) : AccountEditorComponent(), ComponentContext by componentContext {
    private val snackbarManager = snackbarManager()
    private val coroutineScope = componentCoroutineScope()

    init {
        getInitData()
    }

    override fun handleViewAction(action: AccountEditorViewAction) {
        when (action) {
            AccountEditorViewAction.BackClick -> onAction(AccountEditorComponentAction.BackClick)
            AccountEditorViewAction.SaveClick -> saveAccount()
            AccountEditorViewAction.ManageDropdown -> onManageDropdown()
            is AccountEditorViewAction.SelectCurrency -> onSelectCurrency(currency = action.currency)
            is AccountEditorViewAction.UpdateCurrentAmount -> onUpdateCurrentAmount(amount = action.amount)
            is AccountEditorViewAction.UpdateName -> onUpdateName(name = action.name)
        }
    }

    private fun onSelectCurrency(currency: Currency) {
        reduceState { copy(selectedCurrency = currency, isDropdownExpanded = false) }
    }

    private fun onUpdateCurrentAmount(amount: String) {
        amount.isValidAmount().fold(
            onSuccess = { reduceState { copy(amount = it, amountError = "") } },
            onFailure = { reduceState { copy(amountError = dependencies.resourceManager.getString(R.string.account_editor_amount_is_not_digit_error)) } }
        )
    }

    private fun onUpdateName(name: String) {
        reduceState { copy(name = name, nameError = "") }
    }

    private fun onManageDropdown() {
        reduceState { copy(isDropdownExpanded = !isDropdownExpanded) }
    }

    private suspend fun getAccount() {
        val result =
            dependencies.accountRepository.getAccountById(dependencies.args.accountId)

        result.fold(
            onSuccess = {
                reduceState {
                    copy(
                        name = it.name,
                        amount = it.amount.toString(),
                        selectedCurrency = it.currency,
                    )
                }
            },
            onFailure = { snackbarManager.showSnackbar(it.message ?: "") },
        )
    }

    private suspend fun getCurrencies() {
        val result = dependencies.currencyRepository.getCurrencies()

        result.fold(
            onSuccess = {
                reduceState {
                    copy(
                        currencies = it,
                        selectedCurrency = if (selectedCurrency.id == 0) it[0] else selectedCurrency
                    )
                }
            },
            onFailure = { snackbarManager.showSnackbar(it.message ?: "") }
        )
    }

    private fun getInitData() {
        coroutineScope.launch {
            val currenciesDeferred = async { getCurrencies() }

            if (dependencies.args.accountId != -1) {
                reduceState { copy(isLoading = true) }
                val accountDeferred = async { getAccount() }
                accountDeferred.await()
                reduceState { copy(isLoading = false) }
            }

            currenciesDeferred.await()
        }
    }

    private fun saveAccount() {
        validateUserInput {
            coroutineScope.launch {
                reduceState { copy(buttonIsLoading = true) }
                val result = if (dependencies.args.accountId != -1) {
                    dependencies.accountRepository.updateAccount(
                        account = Account(
                            id = dependencies.args.accountId,
                            name = state.value.name,
                            amount = state.value.amount.toLong(),
                            iconId = "",
                            currency = state.value.selectedCurrency,
                        )
                    )
                } else {
                    dependencies.accountRepository.createAccount(
                        account = Account(
                            id = 0,
                            name = state.value.name,
                            amount = state.value.amount.toLong(),
                            iconId = "",
                            currency = state.value.selectedCurrency
                        )
                    )
                }

                result.fold(
                    onSuccess = { onAction(AccountEditorComponentAction.SaveAccount(account = it)) },
                    onFailure = { snackbarManager.showSnackbar(message = it.message ?: "") }
                )

                reduceState { copy(buttonIsLoading = false) }
            }
        }
    }

    private fun validateUserInput(block: () -> Unit) {
        var isValid = true

        if (state.value.name.isEmpty()) {
            reduceState { copy(nameError = dependencies.resourceManager.getString(R.string.account_editor_field_empty_error)) }
            isValid = false
        }


        if (state.value.amount.isEmpty()) {
            reduceState { copy(amountError = dependencies.resourceManager.getString(R.string.account_editor_field_empty_error)) }
            isValid = false
        }

        if (isValid) {
            block()
        }
    }
}