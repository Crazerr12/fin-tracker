package ru.crazerr.feature.budgets.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.InvalidationTracker
import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.budgets.data.dataSource.BudgetLocalDataSource
import ru.crazerr.feature.budgets.data.paging.BudgetPagingSource
import ru.crazerr.feature.budgets.domain.model.TotalBudget
import ru.crazerr.feature.budgets.domain.repository.BudgetRepository
import ru.crazerr.feature.domain.api.Budget
import java.time.LocalDate

internal class BudgetRepositoryImpl(
    private val budgetLocalDataSource: BudgetLocalDataSource,
) : BudgetRepository {

    private var budgetPagingSource: BudgetPagingSource? = null

    init {
        budgetLocalDataSource.addObserver(observer = object :
            InvalidationTracker.Observer("budgets") {
            override fun onInvalidated(tables: Set<String>) {
                budgetPagingSource?.invalidate()
            }
        })
    }

    override fun getBudgetsForCurrentDate(date: LocalDate): Flow<PagingData<Budget>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                BudgetPagingSource(
                    date = date,
                    budgetLocalDataSource = budgetLocalDataSource
                ).also { budgetPagingSource = it }
            }
        ).flow
    }

    override fun getTotalBudget(date: LocalDate): Result<Flow<TotalBudget>> =
        budgetLocalDataSource.getTotalBudget(date = date)
}