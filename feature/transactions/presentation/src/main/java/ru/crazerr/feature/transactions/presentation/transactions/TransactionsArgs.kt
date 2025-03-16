package ru.crazerr.feature.transactions.presentation.transactions

import kotlinx.coroutines.flow.MutableSharedFlow

class TransactionsArgs(
    val inputFlow: MutableSharedFlow<TransactionsComponent.Input>,
)