package ru.crazerr.feature.analysis.presentation.analysisStory

import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

interface AnalysisStoryComponentAction {
    data class OnCategoryClick(
        val categoryId: Long,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val transactionType: TransactionType,
    ) : AnalysisStoryComponentAction
}