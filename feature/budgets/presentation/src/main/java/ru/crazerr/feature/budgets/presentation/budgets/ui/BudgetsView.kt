package ru.crazerr.feature.budgets.presentation.budgets.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.ErrorView
import ru.crazerr.core.utils.components.LoadingView
import ru.crazerr.core.utils.date.toMonthYearFormat
import ru.crazerr.core.utils.presentation.toAmountFormat
import ru.crazerr.feature.budgets.presentation.R
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponent
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsState
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsViewAction
import ru.crazerr.feature.domain.api.Budget

@Composable
fun BudgetsView(modifier: Modifier = Modifier, component: BudgetsComponent) {
    val state by component.state.subscribeAsState()

    BudgetsViewContent(
        modifier = modifier,
        state = state,
        handleViewAction = component::handleViewAction
    )
}

@Composable
private fun BudgetsViewContent(
    modifier: Modifier = Modifier,
    state: BudgetsState,
    handleViewAction: (BudgetsViewAction) -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        floatingActionButton = {
            FloatingActionButton(onClick = { handleViewAction(BudgetsViewAction.GoToBudgetEditor()) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.budgets_add_new_budget_content_description),
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) { paddingValues ->
        BudgetsViewContentBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            state = state,
            handleViewAction = handleViewAction,
        )
    }
}

@Composable
private fun BudgetsViewContentBody(
    modifier: Modifier = Modifier,
    state: BudgetsState,
    handleViewAction: (BudgetsViewAction) -> Unit,
) {
    val budgets = state.budgets.collectAsLazyPagingItems()
    LazyColumn(
        modifier = modifier,
    ) {
        item { CurrentMonthCard(state = state, handleViewAction = handleViewAction) }

        item {
            AnimatedVisibility(
                visible = !state.totalBudgetIsLoading,
                enter = fadeIn(animationSpec = tween(150, 150)),
                exit = fadeOut(animationSpec = tween(150, 150)),
            ) {
                TotalBudgetCard(state = state)
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        budgetItems(budgets = budgets, handleViewAction = handleViewAction)
    }
}

private fun LazyListScope.budgetItems(
    budgets: LazyPagingItems<Budget>,
    handleViewAction: (BudgetsViewAction) -> Unit,
) {
    val loadStateRefresh = budgets.loadState.refresh
    val loadStateAppend = budgets.loadState.append

    when {
        loadStateRefresh is LoadState.Loading -> {
            item { LoadingView(modifier = Modifier.fillMaxHeight()) }
        }

        loadStateRefresh is LoadState.Error -> item {
            ErrorView(
                message = loadStateRefresh.error.localizedMessage ?: "",
                onRetry = budgets::retry,
            )
        }

        loadStateRefresh is LoadState.NotLoading && budgets.itemCount < 1 -> item {
            EmptyBudgets()
        }

        loadStateRefresh is LoadState.NotLoading -> {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.budgets_category),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            items(budgets.itemCount, key = { budgets[it]?.id ?: -1 }) { index ->
                if (budgets[index] != null) {
                    BudgetCardItem(
                        budget = budgets[index]!!,
                        onClick = { handleViewAction(BudgetsViewAction.GoToBudgetEditor(budgetId = it)) },
                        shape = if (index == budgets.itemCount - 1) RoundedCornerShape(
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        ) else RectangleShape,
                        paddingValues = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = if (index == budgets.itemCount - 1) 16.dp else 0.dp,
                        )
                    )
                }
            }

            item {
                if (loadStateAppend is LoadState.Loading) {
                    LoadingView()
                } else if (loadStateAppend is LoadState.Error) {
                    ErrorView(
                        message = loadStateAppend.error.localizedMessage ?: "",
                        onRetry = budgets::retry,
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentMonthCard(
    modifier: Modifier = Modifier,
    state: BudgetsState,
    handleViewAction: (BudgetsViewAction) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp),
                onClick = { handleViewAction(BudgetsViewAction.PreviousMonthClick) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.budgets_previous_month_content_description),
                )
            }

            Text(
                text = state.date.toMonthYearFormat(),
                style = MaterialTheme.typography.titleLarge,
            )

            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 6.dp),
                onClick = { handleViewAction(BudgetsViewAction.NextMonthClick) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.budgets_previous_month_content_description),
                )
            }
        }
    }
}

@Composable
private fun TotalBudgetCard(modifier: Modifier = Modifier, state: BudgetsState) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        if (state.totalBudgetIsLoading) {
            LoadingView()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.budgets_total_budget_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        )

                        Text(
                            text = state.date.toMonthYearFormat(),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    Text(
                        text = state.totalMaxBudget.toAmountFormat(currencySign = '₽'),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { state.currentMaxBudget.toFloat() / state.totalMaxBudget.toFloat() },
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(
                        R.string.budgets_spent_money,
                        state.currentMaxBudget.toAmountFormat('₽'),
                        state.totalMaxBudget.toAmountFormat('₽'),
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun EmptyBudgets(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.budgets_empty),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun BudgetCardItem(
    modifier: Modifier = Modifier,
    budget: Budget,
    onClick: (Long) -> Unit,
    shape: Shape = RectangleShape,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    Card(
        modifier = modifier,
        shape = shape,
        onClick = { onClick(budget.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = Color(budget.category.color).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier = Modifier.size(20.dp),
                    model = budget.category.iconModel.icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = Color(budget.category.color)),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = budget.category.name, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(2.dp))

                LinearProgressIndicator(progress = { budget.currentAmount.toFloat() / budget.maxAmount.toFloat() })

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${budget.currentAmount.toAmountFormat('₽')} / ${
                        budget.maxAmount.toAmountFormat(
                            '₽'
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}