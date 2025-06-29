package ru.crazerr.feature.main.presentation.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.navigation.hideBottomBar
import ru.crazerr.core.utils.navigation.showBottomBar
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.main.presentation.R
import ru.crazerr.feature.main.presentation.main.MainComponentAction.*
import java.util.concurrent.atomic.AtomicInteger

class MainComponent(
    componentContext: ComponentContext,
    private val onAction: (MainComponentAction) -> Unit,
    private val dependencies: MainDependencies,
) : ComponentContext by componentContext,
    BaseComponent<MainState, MainViewAction>(InitialMainState) {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        doOnStart { showBottomBar() }
        doOnStop { hideBottomBar() }
        getInitData()
    }

    override fun handleViewAction(action: MainViewAction) {
        when (action) {
            is MainViewAction.DeleteAccount -> onDeleteAccountById()
            is MainViewAction.GoToAccount -> onAction(GoToAccount(id = action.id))
            MainViewAction.CancelDeleteAccount -> onCancelDeleteAccount()
            is MainViewAction.ShowDeleteAccountDialog -> onShowDeleteAccountDialog(
                account = action.account,
                index = action.index,
            )
        }
    }

    private fun onCancelDeleteAccount() {
        reduceState {
            copy(
                isDeleteAccountDialogVisible = false,
                deletableAccount = null,
                deletableAccountIndex = -1,
            )
        }
    }

    private fun onShowDeleteAccountDialog(account: Account, index: Int) {
        reduceState {
            copy(
                isDeleteAccountDialogVisible = true,
                deletableAccount = account,
                deletableAccountIndex = index,
            )
        }
    }

    private fun onDeleteAccountById() {
        coroutineScope.launch {
            reduceState { copy(accounts = accounts.filter { it.id != deletableAccount?.id }) }

            val result =
                dependencies.accountRepository.deleteAccountById(id = state.value.deletableAccount!!.id)

            result.fold(
                onSuccess = {
                    snackbarManager.showSnackbar(
                        message = dependencies.resourceManager.getString(
                            R.string.main_snackbar_title_account_deleted_success
                        )
                    )
                },
                onFailure = {
                    snackbarManager.showSnackbar(
                        message = dependencies.resourceManager.getString(
                            R.string.main_snackbar_title_account_deleted_failure
                        )
                    )

                    reduceState {
                        val mutableAccounts = mutableListOf<Account>()
                        mutableAccounts.addAll(accounts)
                        mutableAccounts.add(deletableAccountIndex, deletableAccount!!)
                        copy(accounts = mutableAccounts)
                    }
                },
            )
        }
    }

    private fun getInitData() {
        coroutineScope.launch {
            reduceState { copy(isLoading = true) }
            val remainingInitialData = AtomicInteger(2)

            launch {
                var isInitialEmission = true
                dependencies.accountRepository.getAccounts().collect { result ->
                    result.fold(
                        onSuccess = { accounts ->
                            reduceState {
                                copy(
                                    accounts = accounts,
                                    currentAmount = accounts.sumOf { it.amount }
                                )
                            }
                        },
                        onFailure = {
                            snackbarManager.showSnackbar(it.localizedMessage ?: "")
                        }
                    )

                    if (isInitialEmission) {
                        isInitialEmission = false
                        if (remainingInitialData.decrementAndGet() == 0) {
                            reduceState { copy(isLoading = false) }
                        }
                    }
                }
            }

            launch {
                var isInitialEmission = true
                dependencies.balanceRepository.getIncomeAndExpenses().collect { result ->
                    result.fold(
                        onSuccess = { balance ->
                            reduceState {
                                copy(
                                    currentIncome = balance.currentIncome,
                                    currentExpenses = balance.currentExpenses,
                                    lastMonthIncome = balance.lastMonthIncome,
                                    lastMonthExpenses = balance.lastMonthExpenses
                                )
                            }
                        },
                        onFailure = {
                            snackbarManager.showSnackbar(it.localizedMessage ?: "")
                        }
                    )

                    if (isInitialEmission) {
                        isInitialEmission = false
                        if (remainingInitialData.decrementAndGet() == 0) {
                            reduceState { copy(isLoading = false) }
                        }
                    }
                }
            }
        }
    }
}