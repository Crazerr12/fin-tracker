package ru.crazerr.feature.transactions.presentation.transactionsFilter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.DatePickerModal
import ru.crazerr.core.utils.components.Hint
import ru.crazerr.core.utils.components.LoadingView
import ru.crazerr.core.utils.date.toFormatDate
import ru.crazerr.feature.transactions.presentation.R
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponent
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterState
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterViewAction
import ru.crazerr.feature.transactions.presentation.transactionsFilter.model.FilterType
import java.time.LocalDate
import ru.crazerr.core.utils.R as utilsR

@Composable
fun TransactionsFilterView(modifier: Modifier = Modifier, component: TransactionsFilterComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TransactionsFilterTopBar(
                resetButtonIsVisible = state.resetButtonIsVisible,
                handleViewAction = component::handleViewAction,
            )
        },
    ) { paddingValues ->
        TransactionsFilterViewContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            state = state,
            handleViewAction = component::handleViewAction,
        )
    }
}

@Composable
private fun TransactionsFilterViewContent(
    modifier: Modifier = Modifier,
    state: TransactionsFilterState,
    handleViewAction: (TransactionsFilterViewAction) -> Unit,
) {
    when {
        !state.isLoading -> Column(modifier = modifier.fillMaxSize()) {
            TransactionsFilterViewBody(
                modifier = Modifier.weight(1f),
                state = state,
                handleViewAction = handleViewAction,
            )

            if (state.isFilterChanged) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { handleViewAction(TransactionsFilterViewAction.SaveButtonClick) },
                ) {
                    Text(
                        text = stringResource(R.string.transactions_filter_confirm_button),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

        state.isLoading -> LoadingView()
    }
}

@Composable
private fun TransactionsFilterViewBody(
    modifier: Modifier = Modifier,
    state: TransactionsFilterState,
    handleViewAction: (TransactionsFilterViewAction) -> Unit,
) {
    Row(modifier = modifier) {
        FiltersList(
            modifier = Modifier.weight(1f),
            state = state,
            handleViewAction = handleViewAction,
        )

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
        ) {
            when (state.selectedFilterType) {
                FilterType.Account -> if (state.accounts.isNotEmpty()) {
                    ListFilter(
                        title = stringResource(R.string.transactions_filter_account),
                        searchQuery = state.searchQuery,
                        allItemsText = stringResource(R.string.transactions_filter_select_all_accounts),
                        allItemsClick = { handleViewAction(TransactionsFilterViewAction.ManageAllAccounts) },
                        allItemsCheckbox = state.accounts.size == state.selectedAccountIds.size,
                        handleViewAction = handleViewAction,
                        listContent = {
                            val accounts = state.accounts.filter {
                                it.name.contains(
                                    state.searchQuery,
                                    ignoreCase = true
                                )
                            }

                            items(accounts, key = { it.id }) { account ->
                                FilterListItem(
                                    isChecked = state.selectedAccountIds.contains(account.id),
                                    title = account.name,
                                    onClick = {
                                        handleViewAction(
                                            TransactionsFilterViewAction.ManageAccount(
                                                id = account.id
                                            )
                                        )
                                    }
                                )
                            }
                        },
                    )
                } else {
                    EmptyAccounts()
                }

                FilterType.Category -> ListFilter(
                    handleViewAction = handleViewAction,
                    title = stringResource(R.string.transactions_filter_category),
                    searchQuery = state.searchQuery,
                    allItemsText = stringResource(R.string.transactions_filter_select_all_categories),
                    allItemsCheckbox = state.categories.size == state.selectedCategoryIds.size,
                    allItemsClick = { handleViewAction(TransactionsFilterViewAction.ManageAllCategories) },
                    listContent = {
                        val categories =
                            state.categories.filter {
                                it.name.contains(
                                    state.searchQuery,
                                    ignoreCase = true
                                )
                            }

                        items(categories, key = { it.id }) { category ->
                            FilterListItem(
                                isChecked = state.selectedCategoryIds.contains(category.id),
                                title = category.name,
                                onClick = {
                                    handleViewAction(
                                        TransactionsFilterViewAction.ManageCategory(
                                            id = category.id
                                        )
                                    )
                                }
                            )
                        }
                    },
                )

                FilterType.Date -> DateFilter(
                    state = state,
                    handleViewAction = handleViewAction,
                )
            }
        }
    }
}

@Composable
private fun FilterCategoryItem(
    modifier: Modifier = Modifier,
    title: String,
    filterType: FilterType,
    selectedFilterType: FilterType,
    isFilterEnabled: Boolean,
    onClick: (TransactionsFilterViewAction) -> Unit,
) {
    val listItemColor =
        if (selectedFilterType == filterType) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceVariant
    ListItem(
        modifier = modifier.clickable(onClick = {
            onClick(
                TransactionsFilterViewAction.SelectFilterType(
                    filterType
                )
            )
        }),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = listItemColor,
            headlineColor = if (isFilterEnabled) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsFilterTopBar(
    modifier: Modifier = Modifier,
    resetButtonIsVisible: Boolean,
    handleViewAction: (TransactionsFilterViewAction) -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(R.string.transactions_filter_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = { handleViewAction(TransactionsFilterViewAction.BackClick) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(utilsR.string.back_button_content_description)
                )
            }
        },
        actions = {
            if (resetButtonIsVisible) {
                TextButton(onClick = { handleViewAction(TransactionsFilterViewAction.ResetAllFilters) }) {
                    Text(
                        text = stringResource(R.string.transactions_filter_reset_button),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    )
}

@Composable
private fun FiltersList(
    modifier: Modifier = Modifier,
    state: TransactionsFilterState,
    handleViewAction: (TransactionsFilterViewAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        FilterCategoryItem(
            filterType = FilterType.Category,
            title = stringResource(R.string.transactions_filter_category),
            selectedFilterType = state.selectedFilterType,
            isFilterEnabled = state.categories.size != state.selectedCategoryIds.size,
            onClick = handleViewAction,
        )

        FilterCategoryItem(
            filterType = FilterType.Account,
            title = stringResource(R.string.transactions_filter_account),
            selectedFilterType = state.selectedFilterType,
            isFilterEnabled = state.accounts.size != state.selectedAccountIds.size,
            onClick = handleViewAction,
        )

        FilterCategoryItem(
            filterType = FilterType.Date,
            title = stringResource(R.string.transactions_filter_date),
            selectedFilterType = state.selectedFilterType,
            isFilterEnabled = state.startDate != null || state.endDate != null,
            onClick = handleViewAction,
        )
    }
}

@Composable
private fun ListFilter(
    modifier: Modifier = Modifier,
    title: String,
    searchQuery: String,
    allItemsText: String,
    allItemsCheckbox: Boolean,
    allItemsClick: () -> Unit,
    handleViewAction: (TransactionsFilterViewAction) -> Unit,
    listContent: LazyListScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { handleViewAction(TransactionsFilterViewAction.UpdateSearch(it)) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = { Hint(value = stringResource(utilsR.string.search_hint)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = {
                                handleViewAction(
                                    TransactionsFilterViewAction.UpdateSearch(
                                        ""
                                    )
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(utilsR.string.search_clear),
                                )
                            }
                        }
                    } else null,
                    maxLines = 1,
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                FilterListItem(
                    title = allItemsText,
                    isChecked = allItemsCheckbox,
                    onClick = allItemsClick,
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            listContent()
        }
    }
}

@Composable
private fun FilterListItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onClick() }
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateFilter(
    modifier: Modifier = Modifier,
    state: TransactionsFilterState,
    handleViewAction: (TransactionsFilterViewAction) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.transactions_filter_date),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        DateField(
            date = state.startDate,
            dialogIsOpen = state.startDateDialogIsOpen,
            manageDialog = { handleViewAction(TransactionsFilterViewAction.ManageStartDateDialog) },
            hint = stringResource(R.string.transactions_filter_start_date_hint),
            onClear = { handleViewAction(TransactionsFilterViewAction.UpdateStartDate(null)) },
            onDateSelected = { handleViewAction(TransactionsFilterViewAction.UpdateStartDate(it)) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        DateField(
            date = state.endDate,
            dialogIsOpen = state.endDateDialogIsOpen,
            manageDialog = { handleViewAction(TransactionsFilterViewAction.ManageEndDateDialog) },
            hint = stringResource(R.string.transactions_filter_end_date_hint),
            onClear = { handleViewAction(TransactionsFilterViewAction.UpdateEndDate(null)) },
            onDateSelected = { handleViewAction(TransactionsFilterViewAction.UpdateEndDate(it)) },
        )
    }
}

@Composable
private fun EmptyAccounts(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.transactions_filter_accounts_empty),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DateField(
    date: LocalDate?,
    dialogIsOpen: Boolean,
    manageDialog: () -> Unit,
    hint: String,
    onClear: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    val context = LocalContext.current
    var isCloseIconShowing by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(date) {
                awaitEachGesture {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)

                    if (upEvent != null && !isCloseIconShowing) {
                        manageDialog()
                    }
                }
            },
        value = date?.toFormatDate(context) ?: "",
        onValueChange = {},
        readOnly = true,
        label = { Hint(value = hint) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = if (date != null) {
            {
                IconButton(
                    onClick = {
                        onClear()
                        isCloseIconShowing = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(utilsR.string.search_clear)
                    )
                }
            }
        } else {
            null
        }
    )

    if (dialogIsOpen) {
        DatePickerModal(
            initialDate = date,
            onDateSelected = {
                onDateSelected(it)
                isCloseIconShowing = true
            },
            onDismiss = manageDialog,
        )
    }
}
