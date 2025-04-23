package ru.crazerr.feature.transactions.presentation.transactions.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.CategoryIcon
import ru.crazerr.core.utils.components.PagingLazyColumn
import ru.crazerr.core.utils.components.expenseColor
import ru.crazerr.core.utils.components.incomeColor
import ru.crazerr.core.utils.date.toFormatDate
import ru.crazerr.core.utils.presentation.toAmountFormat
import ru.crazerr.feature.transaction.domain.api.Transaction
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transactions.presentation.R
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponent
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsState
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsViewAction
import java.time.LocalDate
import ru.crazerr.core.utils.R as utilsR

@Composable
fun TransactionsView(modifier: Modifier = Modifier, component: TransactionsComponent) {
    val state by component.state.subscribeAsState()

    if (state.dialog) {
        TransactionDialog(handleViewAction = component::handleViewAction)
    }

    TransactionsViewContent(
        modifier = modifier,
        state = state,
        handleViewAction = component::handleViewAction,
    )
}

@Composable
private fun TransactionsViewContent(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    handleViewAction: (TransactionsViewAction) -> Unit
) {
    Scaffold(
        modifier = modifier.statusBarsPadding(),
        topBar = {
            TransactionTopBar(isFilterEnabled = state.isFilterEnabled) {
                handleViewAction(
                    TransactionsViewAction.GoToFilter
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    handleViewAction(
                        TransactionsViewAction.OpenTransactionEditor()
                    )
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.transactions_add_new_transaction_content_description)
                )
            }
        },
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { paddingValues ->
        TransactionsViewContentBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            handleViewAction = handleViewAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionTopBar(
    modifier: Modifier = Modifier,
    isFilterEnabled: Boolean,
    onFilterClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.transactions_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = modifier,
        actions = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = stringResource(R.string.transactions_filter_content_description),
                    tint = if (isFilterEnabled) MaterialTheme.colorScheme.primary else LocalContentColor.current
                )
            }
        },
        windowInsets = WindowInsets(0),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}

@Composable
private fun TransactionsViewContentBody(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    handleViewAction: (TransactionsViewAction) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        TransactionsTabRow(state = state, handleViewAction = handleViewAction)

        Spacer(modifier = Modifier.height(8.dp))

        TransactionList(state = state, handleViewAction = handleViewAction)
    }
}

@Composable
private fun TransactionsTabRow(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    handleViewAction: (TransactionsViewAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        TabItem(
            title = stringResource(R.string.transactions_tabs_all),
            isSelected = state.selectedTransactionType == TransactionType.All,
            onClick = {
                handleViewAction(
                    TransactionsViewAction.SelectTransactionsTypeTab(
                        TransactionType.All
                    )
                )
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        TabItem(
            title = stringResource(R.string.transactions_tabs_income),
            isSelected = state.selectedTransactionType == TransactionType.Income,
            onClick = {
                handleViewAction(
                    TransactionsViewAction.SelectTransactionsTypeTab(
                        TransactionType.Income
                    )
                )
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        TabItem(
            title = stringResource(R.string.transactions_tabs_expenses),
            isSelected = state.selectedTransactionType == TransactionType.Expense,
            onClick = {
                handleViewAction(
                    TransactionsViewAction.SelectTransactionsTypeTab(
                        TransactionType.Expense
                    )
                )
            }
        )

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
private fun TabItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = CircleShape,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Composable
private fun TransactionList(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    handleViewAction: (TransactionsViewAction) -> Unit
) {
    val transactions = state.transactions.collectAsLazyPagingItems()
    PagingLazyColumn(
        modifier = modifier,
        isEmpty = transactions.itemCount < 1,
        loadStates = transactions.loadState,
        empty = { EmptyTransactionsList() },
        onRetry = { transactions.refresh() },
        onRefresh = { transactions.refresh() },
    ) {
        items(
            count = transactions.itemCount,
            key = { transactions[it]?.first ?: LocalDate.now() }
        ) { index ->
            val pair = transactions[index]
            if (pair != null) {
                TransactionCardByDate(
                    date = pair.first,
                    transactions = pair.second,
                    handleViewAction = handleViewAction,
                )
            }
        }
    }
}

@Composable
private fun TransactionCardByDate(
    modifier: Modifier = Modifier,
    date: LocalDate,
    transactions: List<Transaction>,
    handleViewAction: (TransactionsViewAction) -> Unit,
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 6.dp, top = 16.dp),
                text = date.toFormatDate(context),
                style = MaterialTheme.typography.bodyLarge,
            )

            transactions.forEachIndexed { index, transaction ->
                TransactionItem(
                    paddingValues = if (index == transactions.size - 1) PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp,
                        top = 6.dp
                    ) else PaddingValues(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    ),
                    transaction = transaction,
                    onClick = {
                        handleViewAction(
                            TransactionsViewAction.OpenTransactionEditor(
                                transaction.id
                            )
                        )
                    },
                    onLongClick = {
                        handleViewAction(
                            TransactionsViewAction.SelectTransaction(
                                transaction
                            )
                        )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TransactionItem(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        bottom = 6.dp,
        top = 6.dp,
    ),
    transaction: Transaction,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val (sign, color) = if (transaction.type == TransactionType.Income) {
        Pair('+', incomeColor)
    } else {
        Pair('-', expenseColor)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryIcon(color = transaction.category.color, icon = transaction.category.iconModel.icon)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.category.name, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "$sign${
                transaction.amount.toAmountFormat(
                    transaction.account.currency.symbol.getOrElse(
                        index = 0,
                        defaultValue = { '$' }
                    )
                )
            }",
            style = MaterialTheme.typography.bodyMedium.copy(color = color),
        )
    }
}

@Composable
private fun EmptyTransactionsList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.transactions_empty_transactions),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionDialog(
    modifier: Modifier = Modifier,
    handleViewAction: (TransactionsViewAction) -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { handleViewAction(TransactionsViewAction.SelectTransaction(null)) },
    ) {
        ListItem(
            modifier = Modifier.clickable(onClick = { handleViewAction(TransactionsViewAction.DeleteSelectedTransaction) }),
            headlineContent = {
                Text(
                    text = stringResource(utilsR.string.delete),
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = expenseColor,
                )
            },
        )
    }
}