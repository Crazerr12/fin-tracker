package ru.crazerr.feature.budget.data.dataSource

import ru.crazerr.core.database.transactions.dao.TransactionsDao
import java.time.LocalDate

internal class TransactionLocalDataSource(
    private val transactionsDao: TransactionsDao
) {
    suspend fun getAmountSpentByDateAndCategoryId(
        categoryId: Long,
        date: LocalDate
    ): Result<Double> =
        runCatching {
            transactionsDao.getSpentAmountByDateAndCategory(
                categoryId = categoryId,
                date = date.toString()
            )
        }
}