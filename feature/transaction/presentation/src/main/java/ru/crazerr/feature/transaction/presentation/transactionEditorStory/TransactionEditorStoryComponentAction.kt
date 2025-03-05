package ru.crazerr.feature.transaction.presentation.transactionEditorStory

import ru.crazerr.feature.transaction.domain.api.Transaction

sealed interface TransactionEditorStoryComponentAction {
    data object BackClick : TransactionEditorStoryComponentAction

    data class TransactionCreated(val transaction: Transaction) :
        TransactionEditorStoryComponentAction
}