package ru.crazerr.feature.settings.presentation.categories.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.CategoryIcon
import ru.crazerr.core.utils.components.PagingLazyColumn
import ru.crazerr.feature.category.presentation.api.model.CategoryUi
import ru.crazerr.feature.settings.presentation.R
import ru.crazerr.feature.settings.presentation.categories.CategoriesComponent
import ru.crazerr.feature.settings.presentation.categories.CategoriesState
import ru.crazerr.feature.settings.presentation.categories.CategoriesViewAction
import ru.crazerr.core.utils.R as utilsR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesView(modifier: Modifier = Modifier, component: CategoriesComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.categories_top_bar_title),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.handleViewAction(CategoriesViewAction.BackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(utilsR.string.back_button_content_description),
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { component.handleViewAction(CategoriesViewAction.CreateNewCategory) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.categories_fab_content_description),
                )
            }
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        CategoriesContentView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state,
            handleViewAction = component::handleViewAction,
        )
    }
}

@Composable
fun CategoriesContentView(
    modifier: Modifier = Modifier,
    state: CategoriesState,
    handleViewAction: (CategoriesViewAction) -> Unit,
) {
    val categories = state.categories.collectAsLazyPagingItems()

    PagingLazyColumn(
        modifier = modifier,
        isEmpty = categories.itemCount < 1,
        loadStates = categories.loadState,
        empty = { EmptyCategoriesList() },
        onRetry = { categories.refresh() },
        onRefresh = { categories.refresh() },
    ) {
        items(
            count = categories.itemCount,
            key = { categories[it]?.id ?: -1 },
        ) { index ->
            val category = categories[index]
            if (category != null) {
                CategoryItem(handleViewAction = handleViewAction, categoryUi = category)
            }
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    handleViewAction: (CategoriesViewAction) -> Unit,
    categoryUi: CategoryUi,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    handleViewAction(
                        CategoriesViewAction.EditExistCategory(categoryUi = categoryUi)
                    )
                }
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryIcon(color = categoryUi.color, icon = categoryUi.iconUi.icon)

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = categoryUi.name,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun EmptyCategoriesList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.categories_fab_content_description),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}