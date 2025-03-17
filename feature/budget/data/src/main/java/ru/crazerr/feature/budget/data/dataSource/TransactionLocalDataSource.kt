package ru.crazerr.feature.budget.data.dataSource

import ru.crazerr.core.database.transactions.dao.TransactionsDao
import java.time.LocalDate

internal class TransactionLocalDataSource(
    private val transactionsDao: TransactionsDao
) {
    suspend fun getAmountSpentByDateAndCategoryId(categoryId: Int, date: LocalDate): Result<Long> =
        runCatching {
            transactionsDao.getSpentAmountByDateAndCategory(
                categoryId = categoryId,
                date = date.toString()
            )
        }
}