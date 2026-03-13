package ru.crazerr.feature.settings.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.crazerr.feature.category.domain.api.Category
import ru.crazerr.feature.settings.data.dataSource.LocalCategoryDataSource

internal class CategoryPagingSource(
    private val localCategoryDataSource: LocalCategoryDataSource,
) : PagingSource<Int, Category>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Category> {
        val page = params.key ?: INITIAL_PAGE_NUMBER
        val offset = page * params.loadSize

        return try {
            val categories =
                localCategoryDataSource.getCategories(limit = params.loadSize, offset = offset)
                    .getOrThrow()

            LoadResult.Page(
                data = categories,
                prevKey = if (page > INITIAL_PAGE_NUMBER) page - 1 else null,
                nextKey = if (categories.isEmpty()) null else page + 1,
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Category>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val INITIAL_PAGE_NUMBER = 0
    }
}