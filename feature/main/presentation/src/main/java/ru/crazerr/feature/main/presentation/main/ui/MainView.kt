package ru.crazerr.feature.main.presentation.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.LoadingView
import ru.crazerr.core.utils.components.expenseColor
import ru.crazerr.core.utils.components.incomeColor
import ru.crazerr.core.utils.presentation.toAmountFormat
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.main.presentation.R
import ru.crazerr.feature.main.presentation.main.MainComponent
import ru.crazerr.feature.main.presentation.main.MainState
import ru.crazerr.feature.main.presentation.main.MainViewAction

@Composable
fun MainView(modifier: Modifier = Modifier, component: MainComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { component.handleViewAction(MainViewAction.GoToAccount()) },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.main_add_account_content_description)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingView(modifier = Modifier.padding(paddingValues))
        } else {
            MainViewContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = state,
                handleViewAction = component::handleViewAction
            )
        }
    }
}

@Composable
private fun MainViewContent(
    modifier: Modifier = Modifier,
    state: MainState,
    handleViewAction: (MainViewAction) -> Unit
) {
    LazyColumn(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        item {
            MainViewHeader(
                currentAmount = state.currentAmount,
                currencySign = state.mainCurrencySign
            )
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item { IncomeAndExpensesRow(state = state) }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.main_accounts),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        if (state.accounts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.main_empty_accounts),
                        style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                    )
                }
            }
        }

        itemsIndexed(state.accounts) { index, account ->
            AccountCard(account = account, index = index, handleViewAction = handleViewAction)
        }
    }
}

@Composable
private fun MainViewHeader(
    modifier: Modifier = Modifier,
    currentAmount: Double,
    currencySign: Char
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.main_title),
            style = MaterialTheme.typography.titleSmall,
        )

        Text(
            text = currentAmount.toAmountFormat(currencySign = currencySign),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun IncomeAndExpensesRow(modifier: Modifier = Modifier, state: MainState) {
    Row(modifier = modifier.padding(horizontal = 16.dp)) {
        BalanceCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.main_income),
            currentBalance = state.currentIncome,
            lastMonthBalance = state.lastMonthIncome,
            color = incomeColor,
            currencySign = state.mainCurrencySign,
        )

        Spacer(modifier = Modifier.width(12.dp))

        BalanceCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.main_expenses),
            currentBalance = state.currentExpenses,
            lastMonthBalance = state.lastMonthExpenses,
            color = expenseColor,
            currencySign = state.mainCurrencySign,
        )
    }
}

@Composable
private fun BalanceCard(
    modifier: Modifier = Modifier,
    title: String,
    currentBalance: Double,
    lastMonthBalance: Double,
    color: Color,
    currencySign: Char,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = currentBalance.toAmountFormat(currencySign = currencySign),
                style = MaterialTheme.typography.titleMedium.copy(color = color)
            )
        }
    }
}

@Composable
private fun AccountCard(
    modifier: Modifier = Modifier,
    account: Account,
    index: Int,
    handleViewAction: (MainViewAction) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = { handleViewAction(MainViewAction.GoToAccount(account.id)) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (account.icon.id == 21L || account.icon.id == 22L) {
                    AsyncImage(
                        modifier = Modifier.size(24.dp),
                        contentDescription = null,
                        model = account.icon.icon,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier.size(24.dp),
                        contentDescription = null,
                        model = account.icon.icon,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = account.name, style = MaterialTheme.typography.titleSmall)

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        handleViewAction(
                            MainViewAction.DeleteAccount(
                                account = account,
                                index = index
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.main_delete_account_content_description),
                        tint = expenseColor,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = account.amount.toAmountFormat(currencySign = account.currency.symbol[0]),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}