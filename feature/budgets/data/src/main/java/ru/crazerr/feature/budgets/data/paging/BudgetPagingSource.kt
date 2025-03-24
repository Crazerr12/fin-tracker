package ru.crazerr.feature.budgets.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.crazerr.feature.budgets.data.dataSource.BudgetLocalDataSource
import ru.crazerr.feature.domain.api.Budget
import java.time.LocalDate

internal class BudgetPagingSource(
    private val date: LocalDate,
    private val budgetLocalDataSource: BudgetLocalDataSource,
) : PagingSource<Int, Budget>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Budget> {
        val page = params.key ?: INITIAL_PAGE_NUMBER
        val offset = page * params.loadSize

        val data = budgetLocalDataSource.getBudgetsForCurrentDate(
            date = date,
            limit = params.loadSize,
            offset = offset,
        )

        return data.fold(onSuccess = {
            LoadResult.Page(
                data = it,
                prevKey = if (page == INITIAL_PAGE_NUMBER) null else page - 1,
                nextKey = if (it.isEmpty()) null else page + 1
            )
        }, onFailure = { LoadResult.Error(it) })
    }

    override fun getRefreshKey(state: PagingState<Int, Budget>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val INITIAL_PAGE_NUMBER = 0
    }
}