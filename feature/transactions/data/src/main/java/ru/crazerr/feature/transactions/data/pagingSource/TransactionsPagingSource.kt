package ru.crazerr.feature.transactions.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transactions.data.dataSource.LocalTransactionsDataSource
import java.time.LocalDate

internal class TransactionsPagingSource(
    private val categoryIds: IntArray,
    private val accountIds: IntArray,
    private val transactionType: TransactionType,
    private val startDate: LocalDate?,
    private val endDate: LocalDate?,
    private val localTransactionsDataSource: LocalTransactionsDataSource,
) : PagingSource<Int, Pair<LocalDate, List<Transaction>>>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pair<LocalDate, List<Transaction>>> {
        val page = params.key ?: INITIAL_PAGE_NUMBER
        val offset = page * params.loadSize

        return try {
            val dates = localTransactionsDataSource.getPagingDates(
                limit = params.loadSize,
                offset = offset,
                startDate = startDate,
                endDate = endDate
            ).getOrThrow().toList()

            if (dates.isEmpty()) {
                return LoadResult.Page(
                    data = emptyList(),
                    nextKey = null,
                    prevKey = if (page > INITIAL_PAGE_NUMBER) page - 1 else null
                )
            }

            val transactions = localTransactionsDataSource.getTransactionsByFilter(
                categoryIds = categoryIds,
                accountIds = accountIds,
                dates = dates.map { it.toString() },
                transactionType = transactionType
            ).getOrThrow().first()

            val data = dates.mapNotNull { date ->
                if (transactions.any { it.date == date }) {
                    Pair(date, transactions.filter { it.date == date })
                } else {
                    null
                }
            }

            LoadResult.Page(
                data = data,
                prevKey = if (page > INITIAL_PAGE_NUMBER) page - 1 else null,
                nextKey = if (data.isEmpty()) null else page + 1,
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pair<LocalDate, List<Transaction>>>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val INITIAL_PAGE_NUMBER = 0
    }
}