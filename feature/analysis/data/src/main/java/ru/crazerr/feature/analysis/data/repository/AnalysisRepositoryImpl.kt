package ru.crazerr.feature.analysis.data.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.analysis.data.dataSource.AnalysisLocalDataSource
import ru.crazerr.feature.analysis.domain.model.AnalysisCategory
import ru.crazerr.feature.analysis.domain.repository.AnalysisRepository
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

internal class AnalysisRepositoryImpl(
    private val analysisLocalDataSource: AnalysisLocalDataSource,
) : AnalysisRepository {
    override suspend fun getInfo(
        transactionType: TransactionType,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<Flow<Pair<Double, List<AnalysisCategory>>>> =
        analysisLocalDataSource.getInfo(
            transactionType = transactionType,
            startDate = startDate,
            endDate = endDate,
        )
}