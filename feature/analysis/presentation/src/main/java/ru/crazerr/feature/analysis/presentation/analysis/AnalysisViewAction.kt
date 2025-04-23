package ru.crazerr.feature.analysis.presentation.analysis

import ru.crazerr.feature.analysis.domain.model.AnalysisPeriod
import ru.crazerr.feature.transaction.domain.api.TransactionType

sealed interface AnalysisViewAction {
    data class SelectTransactionType(val transactionType: TransactionType) : AnalysisViewAction
    data class SelectAnalysisPeriod(val analysisPeriod: AnalysisPeriod) : AnalysisViewAction

    data object NextDateClick : AnalysisViewAction
    data object PreviousDateClick : AnalysisViewAction

    data object RetryButtonClick : AnalysisViewAction

    data class CategoryClick(val categoryId: Long) : AnalysisViewAction
}