package ru.crazerr.feature.transaction.presentation.transactionEditor

import ru.crazerr.feature.transaction.domain.api.Transaction

sealed interface TransactionEditorComponentAction {
    data object BackClick : TransactionEditorComponentAction

    data object CreateNewCategory : TransactionEditorComponentAction
    data object CreateNewAccount : TransactionEditorComponentAction

    data class SavedClick(val transaction: Transaction) : TransactionEditorComponentAction
}