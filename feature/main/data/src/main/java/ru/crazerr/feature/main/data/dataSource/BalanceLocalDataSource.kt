package ru.crazerr.feature.main.data.dataSource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.transactions.dao.TransactionsDao
import ru.crazerr.feature.main.domain.models.IncomeAndExpenses
import java.time.LocalDateTime

internal class BalanceLocalDataSource(
    private val transactionsDao: TransactionsDao,
) {
    fun getIncomeAndExpenses(): Flow<Result<IncomeAndExpenses>> {
        val date = LocalDateTime.now()

        return transactionsDao.getTransactionsByPeriod(
            date.minusMonths(1).withDayOfMonth(1).toString(),
            date.plusMonths(1).withDayOfMonth(1).minusDays(1).toString()
        ).map { entities ->
            try {
                var lastIncome = 0.0
                var currentIncome = 0.0
                var lastExpenses = 0.0
                var currentExpenses = 0.0

                for (transaction in entities) {
                    when {
                        transaction.date.month == date.month && transaction.type == 0 -> {
                            currentIncome += transaction.amount
                        }

                        transaction.date.month == date.month && transaction.type == 1 -> {
                            currentExpenses += transaction.amount
                        }

                        transaction.date.month != date.month && transaction.type == 0 -> {
                            lastIncome += transaction.amount
                        }

                        transaction.date.month != date.month && transaction.type == 1 -> {
                            lastExpenses += transaction.amount
                        }
                    }
                }

                Result.success(
                    IncomeAndExpenses(
                        currentIncome = currentIncome,
                        lastMonthIncome = lastIncome,
                        currentExpenses = currentExpenses,
                        lastMonthExpenses = lastExpenses
                    )
                )
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }
}