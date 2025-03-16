package ru.crazerr.core.utils.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

@Composable
fun PagingLazyColumn(
    modifier: Modifier = Modifier,
    isEmpty: Boolean,
    loadStates: CombinedLoadStates,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentPadding: PaddingValues = PaddingValues(),
    initialLoading: @Composable () -> Unit = { LoadingView() },
    initialError: @Composable (String, () -> Unit) -> Unit = { message, onRefresh ->
        ErrorView(
            message = message,
            onRetry = onRefresh
        )
    },
    appendLoading: @Composable () -> Unit = { LoadingView() },
    appendError: @Composable (String, () -> Unit) -> Unit = { message, onRetry ->
        ErrorView(message = message, onRetry = onRetry)
    },
    empty: @Composable () -> Unit,
    state: LazyListState = rememberLazyListState(),
    listContent: LazyListScope.() -> Unit,
) {
    val loadStateAppend = loadStates.append
    val loadStateRefresh = loadStates.refresh

    when {
        loadStateRefresh is LoadState.Loading -> initialLoading()
        loadStateRefresh is LoadState.Error -> initialError(
            loadStateRefresh.error.localizedMessage ?: "", onRefresh
        )

        loadStateRefresh is LoadState.NotLoading && isEmpty -> empty()
        loadStateRefresh is LoadState.NotLoading -> LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
        ) {
            listContent()

            item {
                if (loadStateAppend is LoadState.Loading) {
                    appendLoading()
                } else if (loadStateAppend is LoadState.Error) {
                    appendError(loadStateAppend.error.localizedMessage ?: "", onRetry)
                }
            }
        }
    }
}