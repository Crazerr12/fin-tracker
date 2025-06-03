package ru.crazerr.feature.transactions.presentation.transactions

import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.navigation.hideBottomBar
import ru.crazerr.core.utils.navigation.showBottomBar
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponentAction.*
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class TransactionsComponent(
    componentContext: ComponentContext,
    private val onAction: (TransactionsComponentAction) -> Unit,
    private val dependencies: TransactionsDependencies,
) : BaseComponent<TransactionsState, TransactionsViewAction>(InitialTransactionsState),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        doOnStart { showBottomBar() }
        doOnStop { hideBottomBar() }
        handleInputFlow(flow = dependencies.args.inputFlow)
        getInitialData()
    }

    override fun handleViewAction(action: TransactionsViewAction) {
        when (action) {
            is TransactionsViewAction.SelectTransactionsTypeTab -> onSelectTransactionTypeTab(
                transactionType = action.transactionType
            )

            TransactionsViewAction.GoToFilter -> onAction(
                GoToFilter(
                    accountIds = state.value.accountIds,
                    categoryIds = state.value.categoryIds,
                    startDate = state.value.startDate,
                    endDate = state.value.endDate,
                    isFilterEnabled = state.value.isFilterEnabled
                )
            )

            is TransactionsViewAction.OpenTransactionEditor -> onAction(
                OpenTransactionEditor(
                    id = action.id
                )
            )

            TransactionsViewAction.DeleteSelectedTransaction -> onDeleteSelectedTransaction()
            is TransactionsViewAction.SelectTransaction -> onSelectTransaction(transaction = action.transaction)
        }
    }

    private fun onDeleteSelectedTransaction() {
        if (state.value.selectedTransaction != null) {
            coroutineScope.launch {
                val result =
                    dependencies.transactionRepository.deleteTransaction(transaction = state.value.selectedTransaction!!)

                result.fold(
                    onSuccess = {
                        reduceState {
                            copy(
                                dialog = false,
                                selectedTransaction = null,
                            )
                        }
                    },
                    onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
                )
            }
        }
    }

    private fun onSelectTransaction(transaction: Transaction?) {
        reduceState {
            copy(selectedTransaction = transaction, dialog = transaction != null)
        }
    }

    private fun onSelectTransactionTypeTab(transactionType: TransactionType) {
        reduceState { copy(selectedTransactionType = transactionType) }
        getTransactions()
    }

    private fun getInitialData() {
        coroutineScope.launch {
            reduceState {
                copy(
                    accountIds = dependencies.args.accountIds ?: longArrayOf(),
                    categoryIds = dependencies.args.categoryIds ?: longArrayOf(),
                    selectedTransactionType = dependencies.args.transactionType,
                    startDate = dependencies.args.startDate,
                    endDate = dependencies.args.endDate,
                    isFilterEnabled = dependencies.args.accountIds != null || dependencies.args.categoryIds != null
                            || dependencies.args.startDate != null || dependencies.args.endDate != null,
                )
            }

            val remainingInitialData = AtomicInteger(2)
            val transactionsTriggered = AtomicBoolean(false)

            launch {
                dependencies.accountRepository.getAccounts().fold(
                    onSuccess = { flow ->
                        flow.collect { accounts ->
                            reduceState {
                                copy(accountIds = accounts.map { it.id }.toLongArray())
                            }

                            if (remainingInitialData.decrementAndGet() <= 0) {
                                getTransactions()
                            }
                        }
                    },
                    onFailure = {
                        snackbarManager.showSnackbar(it.localizedMessage ?: "")
                    },
                )
            }

            launch {
                dependencies.categoryRepository.getCategories().fold(
                    onSuccess = { flow ->
                        flow.collect { categories ->
                            reduceState {
                                copy(categoryIds = categories.map { it.id }.toLongArray())
                            }

                            if (remainingInitialData.decrementAndGet() <= 0) {
                                getTransactions()
                            }
                        }
                    },
                    onFailure = {
                        snackbarManager.showSnackbar(it.localizedMessage ?: "")
                    },
                )
            }
        }
    }

    private fun getTransactions() {
        val transactionsFlow = dependencies.transactionRepository.getTransactions(
            accountIds = state.value.accountIds,
            categoryIds = state.value.categoryIds,
            transactionType = state.value.selectedTransactionType,
            startDate = state.value.startDate,
            endDate = state.value.endDate,
        ).cachedIn(coroutineScope)

        reduceState { copy(transactions = transactionsFlow) }
    }

    private fun handleInputFlow(flow: MutableSharedFlow<Input>) {
        coroutineScope.launch {
            flow.collectLatest { input ->
                when (input) {
                    is Input.Filter -> handleFilterInput(filter = input)
                    Input.TransactionCreated -> getTransactions()
                }
            }
        }
    }

    private fun handleFilterInput(filter: Input.Filter) {
        reduceState {
            copy(
                accountIds = filter.accountIds,
                categoryIds = filter.categoryIds,
                isFilterEnabled = filter.isFilterEnabled,
                startDate = filter.startDate,
                endDate = filter.endDate,
            )
        }
        getTransactions()
    }

    sealed interface Input {
        data class Filter(
            val accountIds: LongArray,
            val categoryIds: LongArray,
            val startDate: LocalDate?,
            val endDate: LocalDate?,
            val isFilterEnabled: Boolean,
        ) : Input

        data object TransactionCreated : Input
    }
}