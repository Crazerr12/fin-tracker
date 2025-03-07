package ru.crazerr.feature.transaction.presentation.transactionEditor.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.Hint
import ru.crazerr.core.utils.visualTransformations.AmountVisualTransformation
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transaction.presentation.R
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponent
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorState
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorViewAction
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import ru.crazerr.core.utils.R as utilsR

@Composable
fun TransactionEditorView(modifier: Modifier = Modifier, component: TransactionEditorComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TransactionEditorTopBar(
                state = state,
                onBackClick = {
                    component.handleViewAction(
                        TransactionEditorViewAction.BackClick
                    )
                },
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        TransactionEditorViewContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            state = state,
            handleViewAction = component::handleViewAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionEditorTopBar(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(
                    if (state.id != -1) R.string.transaction_editor_top_bar_title_update
                    else R.string.transaction_editor_top_bar_title_create
                ),
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(
                        utilsR.string.back_button_content_description
                    ),
                )
            }
        }
    )
}

@Composable
private fun TransactionEditorViewContent(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    handleViewAction: (TransactionEditorViewAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TransactionEditorCard(state = state, handleViewAction = handleViewAction)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { handleViewAction(TransactionEditorViewAction.SaveClick) }) {
            Text(
                text = stringResource(R.string.transaction_editor_save_button),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun TransactionEditorCard(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    handleViewAction: (TransactionEditorViewAction) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TransactionTypeRow(state = state, handleViewAction = handleViewAction)

            Spacer(modifier = Modifier.height(16.dp))

            AccountDropdown(state = state, handleViewAction = handleViewAction)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.amount,
                onValueChange = { handleViewAction(TransactionEditorViewAction.UpdateAmount(it)) },
                singleLine = true,
                visualTransformation = AmountVisualTransformation(state.selectedAccount.currency.symbol[0]),
                placeholder = { Hint(value = stringResource(R.string.transaction_editor_amount_hint)) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategoryDropdown(state = state, handleViewAction = handleViewAction)

            Spacer(modifier = Modifier.height(16.dp))

            DateRow(state = state, handleViewAction = handleViewAction)
        }
    }
}

@Composable
private fun TransactionTypeRow(
    state: TransactionEditorState,
    handleViewAction: (TransactionEditorViewAction) -> Unit
) {
    Text(
        text = stringResource(R.string.transaction_editor_transaction_type_title),
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        TransactionTypeButton(
            text = stringResource(R.string.transaction_editor_transaction_type_expense),
            icon = ImageVector.vectorResource(R.drawable.ic_minus),
            isSelected = state.transactionType == TransactionType.Expense,
            onSelect = {
                handleViewAction(
                    TransactionEditorViewAction.SelectTransactionType(
                        TransactionType.Expense
                    )
                )
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        TransactionTypeButton(
            text = stringResource(R.string.transaction_editor_transaction_type_income),
            icon = Icons.Default.Add,
            isSelected = state.transactionType == TransactionType.Income,
            onSelect = {
                handleViewAction(
                    TransactionEditorViewAction.SelectTransactionType(
                        TransactionType.Expense
                    )
                )
            }
        )
    }
}

@Composable
private fun TransactionTypeButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) Color.White else Color(0xFF374151)

    Button(
        onClick = onSelect,
        border = BorderStroke(width = 1.dp, color = Color(0xFFE5E7EB)),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        )
    ) {
        Box(modifier = Modifier.size(14.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = containerColor
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDropdown(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    handleViewAction: (TransactionEditorViewAction) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = state.accountsDropdownIsExpanded,
        onExpandedChange = { handleViewAction(TransactionEditorViewAction.ManageAccountDropdown) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = state.selectedAccount.name,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = state.accountsDropdownIsExpanded, modifier = Modifier.menuAnchor(
                        MenuAnchorType.SecondaryEditable
                    )
                )
            },
        )

        ExposedDropdownMenu(
            expanded = state.accountsDropdownIsExpanded,
            onDismissRequest = { handleViewAction(TransactionEditorViewAction.ManageAccountDropdown) }
        ) {
            LazyColumn(
                modifier = Modifier.height(400.dp)
            ) {
                items(state.accounts) { account ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${account.currency.code}-${account.currency.name}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            handleViewAction(
                                TransactionEditorViewAction.SelectAccount(
                                    account
                                )
                            )
                        },
                        trailingIcon = {
                            Text(
                                text = account.currency.symbol,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                    )
                }
            }

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.transaction_editor_account_add),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = { handleViewAction },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    handleViewAction: (TransactionEditorViewAction) -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = state.categoriesDropdownIsExpanded,
        onExpandedChange = { handleViewAction(TransactionEditorViewAction.ManageCategoryDropdown) },
    ) {
        OutlinedTextField(
            value = state.selectedCategory.name,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = state.categoriesDropdownIsExpanded, modifier = Modifier.menuAnchor(
                        MenuAnchorType.SecondaryEditable
                    )
                )
            }
        )

        ExposedDropdownMenu(
            expanded = state.categoriesDropdownIsExpanded,
            onDismissRequest = { handleViewAction(TransactionEditorViewAction.ManageCategoryDropdown) },
        ) {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                items(state.categories) { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            handleViewAction(
                                TransactionEditorViewAction.SelectCategory(
                                    category
                                )
                            )
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(color = Color(category.color).copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = category.iconId,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(color = Color(category.color))
                                )
                            }
                        },
                    )
                }
            }

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.transaction_editor_category_add),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = { handleViewAction(TransactionEditorViewAction.CreateNewCategory) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
private fun DateRow(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    handleViewAction: (TransactionEditorViewAction) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.pointerInput(state.date) {
            awaitEachGesture {
                awaitFirstDown(pass = PointerEventPass.Initial)
                val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                if (upEvent != null) {
                    handleViewAction(TransactionEditorViewAction.ManageDateDialog)
                }
            }
        },
        value = state.date.toString(),
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.transaction_editor_date_content_description)
            )
        },
    )

    if (state.dateDialogIsExpanded) {
        DatePickerModal(
            initialDate = state.date,
            onDateSelected = { handleViewAction(TransactionEditorViewAction.SaveDate(date = it)) },
            onDismiss = { handleViewAction(TransactionEditorViewAction.ManageDateDialog) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = ZonedDateTime.of(
            initialDate, LocalTime.now(), ZoneId.systemDefault()
        ).toInstant().toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(
                        Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: 0L).atZone(
                            ZoneId.systemDefault()
                        ).toLocalDate()
                    )
                },
            ) {
                Text(
                    text = stringResource(utilsR.string.confirm_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(utilsR.string.cancel_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}