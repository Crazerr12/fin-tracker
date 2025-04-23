package ru.crazerr.feature.budgets.presentation.budgets

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.navigation.hideBottomBar
import ru.crazerr.core.utils.navigation.showBottomBar
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponentAction.*
import ru.crazerr.feature.domain.api.Budget

class BudgetsComponent(
    componentContext: ComponentContext,
    private val onAction: (BudgetsComponentAction) -> Unit,
    private val dependencies: BudgetsDependencies,
) : BaseComponent<BudgetsState, BudgetsViewAction>(InitialBudgetsState),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        doOnStart { showBottomBar() }
        doOnStop { hideBottomBar() }
        getData()
    }

    override fun handleViewAction(action: BudgetsViewAction) {
        when (action) {
            is BudgetsViewAction.GoToBudgetEditor -> onAction(
                GoToBudgetEditor(
                    budgetId = action.budgetId
                )
            )

            BudgetsViewAction.NextMonthClick -> onNextMonthClick()
            BudgetsViewAction.PreviousMonthClick -> onPreviousMonthClick()
            BudgetsViewAction.DeleteSelectedBudget -> onDeleteSelectedBudget()
            is BudgetsViewAction.SelectBudget -> onSelectBudget(budget = action.budget)
        }
    }

    private fun onDeleteSelectedBudget() {
        if (state.value.selectedBudget != null) {
            coroutineScope.launch {
                val result =
                    dependencies.budgetRepository.deleteBudget(budget = state.value.selectedBudget!!)

                result.fold(
                    onSuccess = {
                        reduceState {
                            copy(
                                selectedBudget = null,
                                bottomSheet = false,
                            )
                        }
                    },
                    onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
                )
            }
        }
    }

    private fun onSelectBudget(budget: Budget?) {
        reduceState { copy(selectedBudget = budget, bottomSheet = budget != null) }
    }

    private fun onPreviousMonthClick() {
        reduceState { copy(date = date.minusMonths(1)) }
        getData()
    }

    private fun onNextMonthClick() {
        reduceState { copy(date = date.plusMonths(1)) }
        getData()
    }

    private fun getData() {
        coroutineScope.launch {
            val totalBudgetDeferred = async {
                reduceState { copy(totalBudgetIsLoading = true) }
                dependencies.budgetRepository.getTotalBudget(date = state.value.date)
            }

            val budgetsDeferred =
                async { dependencies.budgetRepository.getBudgetsForCurrentDate(date = state.value.date) }

            totalBudgetDeferred.await().fold(
                onSuccess = {
                    coroutineScope.launch {
                        it.collect {
                            reduceState {
                                copy(
                                    totalBudgetIsLoading = false,
                                    totalMaxBudget = it.total,
                                    currentMaxBudget = it.current
                                )
                            }
                        }
                    }
                },
                onFailure = { snackbarManager.showSnackbar(message = it.localizedMessage ?: "") },
            )

            val budgets = budgetsDeferred.await()

            reduceState { copy(budgets = budgets) }
        }
    }
}