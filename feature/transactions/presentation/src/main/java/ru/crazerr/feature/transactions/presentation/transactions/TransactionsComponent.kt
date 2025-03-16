package ru.crazerr.feature.transactions.presentation.transactions

import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

class TransactionsComponent(
    componentContext: ComponentContext,
    private val onAction: (TransactionsComponentAction) -> Unit,
    private val dependencies: TransactionsDependencies,
) : BaseComponent<TransactionsState, TransactionsViewAction>(InitialTransactionsState),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        handleInputFlow(flow = dependencies.args.inputFlow)
        getInitialData()
    }

    override fun handleViewAction(action: TransactionsViewAction) {
        when (action) {
            is TransactionsViewAction.SelectTransactionsTypeTab -> onSelectTransactionTypeTab(
                transactionType = action.transactionType
            )

            TransactionsViewAction.GoToFilter -> onAction(
                TransactionsComponentAction.GoToFilter(
                    accountIds = state.value.accountIds,
                    categoryIds = state.value.categoryIds,
                    startDate = state.value.startDate,
                    endDate = state.value.endDate,
                    isFilterEnabled = state.value.isFilterEnabled
                )
            )

            is TransactionsViewAction.OpenTransactionEditor -> onAction(
                TransactionsComponentAction.OpenTransactionEditor(
                    id = action.id
                )
            )
        }
    }

    private fun onSelectTransactionTypeTab(transactionType: TransactionType) {
        reduceState { copy(selectedTransactionType = transactionType) }
        getTransactions()
    }

    private fun getInitialData() {
        coroutineScope.launch {
            val accountsDeferred = async { dependencies.accountRepository.getAccounts() }
            val categoriesDeferred = async { dependencies.categoryRepository.getCategories() }

            accountsDeferred.await().fold(
                onSuccess = { reduceState { copy(accountIds = it.map { it.id }.toIntArray()) } },
                onFailure = { snackbarManager.showSnackbar(message = it.localizedMessage ?: "") }
            )
            categoriesDeferred.await().fold(
                onSuccess = { reduceState { copy(categoryIds = it.map { it.id }.toIntArray()) } },
                onFailure = { snackbarManager.showSnackbar(message = it.localizedMessage ?: "") }
            )

            getTransactions()
        }
    }

    private fun getTransactions() {
        val transactionsFlow = dependencies.transactionRepository.getTransactions(
            accountIds = state.value.accountIds,
            categoryIds = state.value.categoryIds,
            transactionType = state.value.selectedTransactionType,
            startDate = state.value.startDate,
            endDate = state.value.endDate
        ).cachedIn(coroutineScope)

        reduceState { copy(transactions = transactionsFlow) }
    }

    private fun handleInputFlow(flow: MutableSharedFlow<Input>) {
        coroutineScope.launch {
            flow.collectLatest { input ->
                when (input) {
                    is Input.Filter -> handleFilterInput(filter = input)
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
            val accountIds: IntArray,
            val categoryIds: IntArray,
            val startDate: LocalDate?,
            val endDate: LocalDate?,
            val isFilterEnabled: Boolean,
        ) : Input
    }
}