package ru.crazerr.feature.analysis.presentation.analysis

import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

sealed interface AnalysisComponentAction {
    data class OnCategoryClick(
        val categoryId: Long,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val transactionType: TransactionType,
    ) : AnalysisComponentAction
}