package ru.crazerr.feature.transactions.presentation.transactions

import ru.crazerr.feature.transaction.domain.api.TransactionType

sealed interface TransactionsViewAction {
    data object GoToFilter : TransactionsViewAction

    data class SelectTransactionsTypeTab(val transactionType: TransactionType) :
        TransactionsViewAction

    data class OpenTransactionEditor(val id: Long = -1) : TransactionsViewAction
}