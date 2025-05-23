package ru.crazerr.feature.transaction.presentation.transactionEditor

import kotlinx.coroutines.flow.MutableSharedFlow

data class TransactionEditorArgs(
    val id: Long = -1,
    val input: MutableSharedFlow<TransactionEditorComponent.Input>,
)
