package ru.crazerr.feature.analysis.presentation.analysis

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.navigation.hideBottomBar
import ru.crazerr.core.utils.navigation.showBottomBar
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.feature.analysis.domain.model.AnalysisPeriod
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

class AnalysisComponent(
    componentContext: ComponentContext,
    private val onAction: (AnalysisComponentAction) -> Unit,
    private val dependencies: AnalysisDependencies,
) : BaseComponent<AnalysisState, AnalysisViewAction>(InitialAnalysisState),
    ComponentContext by componentContext {

    private val coroutineScope = componentCoroutineScope()

    init {
        doOnStart { showBottomBar() }
        doOnStop { hideBottomBar() }
        getInfo()
    }

    override fun handleViewAction(action: AnalysisViewAction) {
        when (action) {
            is AnalysisViewAction.SelectAnalysisPeriod -> onSelectAnalysisPeriod(analysisPeriod = action.analysisPeriod)
            is AnalysisViewAction.SelectTransactionType -> onSelectTransactionType(transactionType = action.transactionType)
            AnalysisViewAction.NextDateClick -> onNextDateClick()
            AnalysisViewAction.PreviousDateClick -> onPreviousDateClick()
            AnalysisViewAction.RetryButtonClick -> getInfo()
            is AnalysisViewAction.CategoryClick -> onCategoryClick(categoryId = action.categoryId)
        }
    }

    private fun onPreviousDateClick() {
        when (state.value.selectedAnalysisPeriod) {
            AnalysisPeriod.Month -> reduceState { copy(date = date.minusMonths(1)) }
            AnalysisPeriod.Week -> reduceState { copy(date = date.minusWeeks(1)) }
            AnalysisPeriod.Year -> reduceState { copy(date = date.minusYears(1)) }
        }
        getInfo()
    }

    private fun onNextDateClick() {
        when (state.value.selectedAnalysisPeriod) {
            AnalysisPeriod.Month -> reduceState { copy(date = date.plusMonths(1)) }
            AnalysisPeriod.Week -> reduceState { copy(date = date.plusWeeks(1)) }
            AnalysisPeriod.Year -> reduceState { copy(date = date.plusYears(1)) }
        }
        getInfo()
    }

    private fun onSelectAnalysisPeriod(analysisPeriod: AnalysisPeriod) {
        reduceState {
            copy(
                selectedAnalysisPeriod = analysisPeriod,
                date = if (analysisPeriod != selectedAnalysisPeriod) LocalDate.now() else date,
            )
        }
        getInfo()
    }

    private fun onSelectTransactionType(transactionType: TransactionType) {
        reduceState { copy(selectedTransactionType = transactionType) }
        getInfo()
    }

    private fun getInfo() {
        coroutineScope.launch {
            reduceState { copy(isLoading = true) }
            val result = dependencies.analysisRepository.getInfo(
                transactionType = state.value.selectedTransactionType,
                startDate = state.value.selectedAnalysisPeriod.getStartDate(state.value.date),
                endDate = state.value.selectedAnalysisPeriod.getEndDate(state.value.date),
            )

            result.fold(
                onSuccess = { flow ->
                    flow.collect { pair ->
                        reduceState {
                            copy(
                                totalAmount = pair.first,
                                analysisCategories = pair.second,
                                isLoading = false,
                            )
                        }
                    }
                },
                onFailure = {
                    reduceState {
                        copy(
                            error = it.localizedMessage ?: "error",
                            isLoading = false,
                        )
                    }
                },
            )
        }
    }

    private fun onCategoryClick(categoryId: Long) {
        onAction(
            AnalysisComponentAction.OnCategoryClick(
                categoryId = categoryId,
                startDate = state.value.selectedAnalysisPeriod.getStartDate(state.value.date),
                endDate = state.value.selectedAnalysisPeriod.getEndDate(state.value.date),
                transactionType = state.value.selectedTransactionType,
            )
        )
    }
}