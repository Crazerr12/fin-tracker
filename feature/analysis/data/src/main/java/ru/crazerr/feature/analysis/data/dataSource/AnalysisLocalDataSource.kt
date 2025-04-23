package ru.crazerr.feature.analysis.data.dataSource

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.analysis.data.mapper.toAnalysisCategory
import ru.crazerr.feature.analysis.domain.model.AnalysisCategory
import ru.crazerr.feature.transaction.domain.api.TransactionType
import java.time.LocalDate

internal class AnalysisLocalDataSource(
    private val appDatabase: AppDatabase,
) {
    private val transactionsDao = appDatabase.transactionsDao()

    suspend fun getInfo(
        transactionType: TransactionType,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<Flow<Pair<Double, List<AnalysisCategory>>>> = runCatching {
        appDatabase.withTransaction {
            val totalAmountFlow = transactionsDao.getTotalAmountByPeriodAndType(
                transactionType = transactionType.id,
                startDate = startDate,
                endDate = endDate
            )

            val analysisCategoriesFlow = transactionsDao.getAnalysisCategoriesByDateAndType(
                startDate = startDate,
                endDate = endDate,
                transactionType = transactionType.id
            ).map { it.map { it.toAnalysisCategory() } }

            totalAmountFlow.combine(analysisCategoriesFlow) { totalAmount, analysisCategories ->
                Pair(totalAmount, analysisCategories)
            }
        }
    }
}