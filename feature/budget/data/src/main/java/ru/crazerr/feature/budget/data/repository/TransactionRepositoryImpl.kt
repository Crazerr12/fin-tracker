package ru.crazerr.feature.budget.data.repository

import ru.crazerr.feature.budget.data.dataSource.TransactionLocalDataSource
import ru.crazerr.feature.budget.domain.TransactionRepository
import java.time.LocalDate

internal class TransactionRepositoryImpl(
    private val transactionLocalDataSource: TransactionLocalDataSource,
) : TransactionRepository {
    override suspend fun getCurrentAmount(categoryId: Int, date: LocalDate): Result<Long> =
        transactionLocalDataSource.getAmountSpentByDateAndCategoryId(
            categoryId = categoryId,
            date = date,
        )
}