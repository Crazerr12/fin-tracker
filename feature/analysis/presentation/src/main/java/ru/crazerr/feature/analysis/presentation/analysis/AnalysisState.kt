package ru.crazerr.feature.analysis.presentation.analysis

import ru.crazerr.feature.analysis.domain.model.AnalysisCategory
import ru.crazerr.feature.analysis.domain.model.AnalysisPeriod
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

data class AnalysisState(
    val selectedTransactionType: TransactionType,
    val selectedAnalysisPeriod: AnalysisPeriod,
    val date: LocalDate,
    val totalAmount: Double,
    val analysisCategories: List<AnalysisCategory>,
    val isLoading: Boolean,
    val error: String,
)

internal val InitialAnalysisState = AnalysisState(
    selectedTransactionType = TransactionType.Expense,
    selectedAnalysisPeriod = AnalysisPeriod.Week,
    date = LocalDate.now(),
    totalAmount = 0.0,
    analysisCategories = emptyList(),
    isLoading = true,
    error = "",
)