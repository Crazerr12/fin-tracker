package ru.crazerr.feature.budget.data.repository

import ru.crazerr.feature.budget.data.dataSource.TransactionLocalDataSource
import ru.crazerr.feature.budget.domain.TransactionRepository
import java.time.LocalDate

internal class TransactionRepositoryImpl(
    private val transactionLocalDataSource: TransactionLocalDataSource,
) : TransactionRepository {
    override suspend fun getCurrentAmount(categoryId: Long, date: LocalDate): Result<Double> =
        transactionLocalDataSource.getAmountSpentByDateAndCategoryId(
            categoryId = categoryId,
            date = date,
        )
}