package ru.crazerr.feature.transaction.presentation.transactionEditor

import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.currency.domain.api.Currency
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

data class TransactionEditorState(
    val id: Int,
    val transactionType: TransactionType,
    val accounts: List<Account>,
    val selectedAccount: Account,
    val accountsDropdownIsExpanded: Boolean,
    val amount: String,
    val amountError: String,
    val categories: List<Category>,
    val selectedCategory: Category,
    val categoriesDropdownIsExpanded: Boolean,
    val date: LocalDate,
    val dateDialogIsExpanded: Boolean,
    val buttonIsLoading: Boolean,
)

internal val InitialTransactionEditorState = TransactionEditorState(
    id = -1,
    transactionType = TransactionType.Expense,
    accounts = emptyList(),
    selectedAccount = Account(
        id = 0,
        name = "",
        amount = 0,
        iconId = "",
        currency = Currency(id = 0, name = "", symbol = "", code = "")
    ),
    amount = "",
    amountError = "",
    categories = emptyList(),
    selectedCategory = Category(
        id = 0, name = "", color = 0L, iconId = "", isTemplate = true,
    ),
    date = LocalDate.now(),
    categoriesDropdownIsExpanded = false,
    accountsDropdownIsExpanded = false,
    dateDialogIsExpanded = false,
    buttonIsLoading = false,
)
