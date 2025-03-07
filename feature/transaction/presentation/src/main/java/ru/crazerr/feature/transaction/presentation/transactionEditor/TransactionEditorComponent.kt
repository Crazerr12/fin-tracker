package ru.crazerr.feature.transaction.presentation.transactionEditor

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transaction.presentation.R
import java.time.LocalDate

class TransactionEditorComponent(
    componentContext: ComponentContext,
    private val onAction: (TransactionEditorComponentAction) -> Unit,
    private val dependencies: TransactionEditorDependencies,
) : BaseComponent<TransactionEditorState, TransactionEditorViewAction>(InitialTransactionEditorState),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        if (dependencies.args.id != -1) {
            getTransaction()
        }
        getAccountsAndCategories()
        handleInput(input = dependencies.args.input)
    }

    override fun handleViewAction(action: TransactionEditorViewAction) {
        when (action) {
            TransactionEditorViewAction.BackClick -> onAction(TransactionEditorComponentAction.BackClick)
            TransactionEditorViewAction.ManageAccountDropdown -> onManageAccountDropdown()
            TransactionEditorViewAction.ManageCategoryDropdown -> onManageCategoryDropdown()
            TransactionEditorViewAction.SaveClick -> onSaveClick()
            is TransactionEditorViewAction.SelectAccount -> onSelectAccount(account = action.account)
            is TransactionEditorViewAction.SelectCategory -> onSelectCategory(category = action.category)
            is TransactionEditorViewAction.SelectTransactionType -> onSelectTransactionType(
                transactionType = action.transactionType
            )

            is TransactionEditorViewAction.UpdateAmount -> onUpdateAmount(amount = action.amount)
            TransactionEditorViewAction.CreateNewAccount -> onAction(
                TransactionEditorComponentAction.CreateNewAccount
            )

            TransactionEditorViewAction.CreateNewCategory -> onAction(
                TransactionEditorComponentAction.CreateNewCategory
            )

            TransactionEditorViewAction.ManageDateDialog -> onManageDateDialog()
            is TransactionEditorViewAction.SaveDate -> onSaveDate(date = action.date)
        }
    }

    private fun onSaveDate(date: LocalDate) {
        reduceState { copy(date = date) }
    }

    private fun onManageDateDialog() {
        reduceState { copy(dateDialogIsExpanded = !dateDialogIsExpanded) }
    }

    private fun onManageAccountDropdown() {
        reduceState { copy(accountsDropdownIsExpanded = !accountsDropdownIsExpanded) }
    }

    private fun onUpdateAmount(amount: String) {
        if (amount.any { !it.isDigit() || it != '.' } && amount.count { it == '.' } < 2) {
            reduceState { copy(amount = amount, amountError = "") }
        } else {
            reduceState { copy(amountError = dependencies.resourceManager.getString(R.string.transaction_editor_amount_only_digits_error)) }
        }
    }

    private fun onSelectAccount(account: Account) {
        reduceState { copy(selectedAccount = account, accountsDropdownIsExpanded = false) }
    }

    private fun onSelectCategory(category: Category) {
        reduceState { copy(selectedCategory = category, categoriesDropdownIsExpanded = false) }
    }

    private fun onSelectTransactionType(transactionType: TransactionType) {
        reduceState { copy(transactionType = transactionType) }
    }

    private fun onManageCategoryDropdown() {
        reduceState { copy(categoriesDropdownIsExpanded = !categoriesDropdownIsExpanded) }
    }

    private fun onSaveClick() {
        validateUserInput {
            coroutineScope.launch {
                reduceState { copy(buttonIsLoading = true) }

                val result = if (state.value.id != -1) {
                    dependencies.transactionRepository.createTransaction(
                        transaction = Transaction(
                            id = state.value.id,
                            category = state.value.selectedCategory,
                            amount = state.value.amount.toLong(),
                            type = state.value.transactionType,
                            date = state.value.date,
                            account = state.value.selectedAccount
                        )
                    )
                } else {
                    dependencies.transactionRepository.updateTransaction(
                        transaction = Transaction(
                            id = state.value.id,
                            category = state.value.selectedCategory,
                            amount = state.value.amount.toLong(),
                            type = state.value.transactionType,
                            date = state.value.date,
                            account = state.value.selectedAccount,
                        )
                    )
                }

                result.fold(
                    onSuccess = { onAction(TransactionEditorComponentAction.SavedClick(it)) },
                    onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") }
                )

                reduceState { copy(buttonIsLoading = false) }
            }
        }
    }

    private fun getTransaction() {
        coroutineScope.launch {
            val result =
                dependencies.transactionRepository.getTransactionById(id = dependencies.args.id)

            result.fold(
                onSuccess = {
                    reduceState {
                        copy(
                            id = it.id,
                            transactionType = it.type,
                            selectedAccount = it.account,
                            amount = it.amount.toString(),
                            selectedCategory = it.category,
                            date = it.date,
                        )
                    }
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )
        }
    }

    private fun getAccountsAndCategories() {
        coroutineScope.launch {
            val accountsResult = async { dependencies.accountRepository.getAccounts() }
            val categoriesResult = async { dependencies.categoryRepository.getCategories() }

            accountsResult.await().collect {
                it.fold(
                    onSuccess = { reduceState { copy(accounts = it) } },
                    onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") }
                )
            }

            categoriesResult.await().fold(
                onSuccess = { reduceState { copy(categories = it) } },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") }
            )
        }
    }

    private fun validateUserInput(block: () -> Unit) {
        var isValid = true

        if (state.value.amount.isBlank()) {
            reduceState { copy(amountError = dependencies.resourceManager.getString(R.string.transaction_editor_amount_empty_error)) }
            isValid = false
        }

        if (isValid) {
            block()
        }
    }

    private fun handleInput(input: MutableSharedFlow<Input>) {
        coroutineScope.launch {
            input.collectLatest {
                when (it) {
                    is Input.AccountInput -> reduceState { copy(selectedAccount = it.account) }
                    is Input.CategoryInput -> reduceState { copy(selectedCategory = it.category) }
                }
            }
        }
    }

    sealed interface Input {
        data class CategoryInput(val category: Category) : Input
        data class AccountInput(val account: Account) : Input
    }
}