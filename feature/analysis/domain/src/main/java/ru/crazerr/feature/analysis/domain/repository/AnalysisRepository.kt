package ru.crazerr.feature.analysis.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.analysis.domain.model.AnalysisCategory
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

interface AnalysisRepository {
    suspend fun getInfo(
        transactionType: TransactionType,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<Flow<Pair<Double, List<AnalysisCategory>>>>
}