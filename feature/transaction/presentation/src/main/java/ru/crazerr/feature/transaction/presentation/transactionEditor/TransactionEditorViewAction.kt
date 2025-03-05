package ru.crazerr.feature.transaction.presentation.transactionEditor

import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

sealed interface TransactionEditorViewAction {
    data object BackClick : TransactionEditorViewAction

    data class SelectTransactionType(val transactionType: TransactionType) :
        TransactionEditorViewAction

    data object ManageAccountDropdown : TransactionEditorViewAction
    data class SelectAccount(val account: Account) : TransactionEditorViewAction
    data object CreateNewAccount : TransactionEditorViewAction

    data class UpdateAmount(val amount: String) : TransactionEditorViewAction

    data object ManageCategoryDropdown : TransactionEditorViewAction
    data class SelectCategory(val category: Category) : TransactionEditorViewAction
    data object CreateNewCategory : TransactionEditorViewAction

    data object ManageDateDialog : TransactionEditorViewAction
    data class SaveDate(val date: LocalDate) : TransactionEditorViewAction

    data object SaveClick : TransactionEditorViewAction

}