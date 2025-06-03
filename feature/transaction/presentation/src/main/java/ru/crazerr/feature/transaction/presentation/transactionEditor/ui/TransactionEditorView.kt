package ru.crazerr.feature.transaction.presentation.transactionEditor.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.DatePickerModal
import ru.crazerr.core.utils.components.Hint
import ru.crazerr.core.utils.date.toFormatDate
import ru.crazerr.core.utils.visualTransformations.AmountVisualTransformation
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transaction.presentation.R
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponent
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorState
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorViewAction
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
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                    if (state.id != -1L) R.string.transaction_editor_top_bar_title_update
                    else R.string.transaction_editor_top_bar_title_create
                ),
                style = MaterialTheme.typography.titleLarge,
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
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        TransactionEditorCard(state = state, handleViewAction = handleViewAction)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            onClick = { handleViewAction(TransactionEditorViewAction.SaveClick) },
        ) {
            Text(
                text = stringResource(R.string.transaction_editor_save_button),
                style = MaterialTheme.typography.titleMedium,
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
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
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
                modifier = Modifier.fillMaxWidth(),
                value = state.amount,
                onValueChange = { handleViewAction(TransactionEditorViewAction.UpdateAmount(it)) },
                singleLine = true,
                supportingText = if (state.amountError.isNotEmpty()) {
                    { Hint(value = state.amountError) }
                } else {
                    null
                },
                isError = state.amountError.isNotEmpty(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Decimal,
                ),
                visualTransformation = AmountVisualTransformation(
                    sign = state.selectedAccount.currency.symbol.getOrElse(
                        index = 0,
                        defaultValue = { Char.MIN_VALUE }),
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                label = { Hint(value = stringResource(R.string.transaction_editor_amount_hint)) },
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
            modifier = Modifier.weight(1f),
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
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.transaction_editor_transaction_type_income),
            icon = Icons.Default.Add,
            isSelected = state.transactionType == TransactionType.Income,
            onSelect = {
                handleViewAction(
                    TransactionEditorViewAction.SelectTransactionType(
                        TransactionType.Income
                    )
                )
            }
        )
    }
}

@Composable
private fun TransactionTypeButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Button(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        onClick = onSelect,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        )
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(shape = CircleShape, color = contentColor),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = containerColor,
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
    Column(modifier = modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
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
                isError = state.selectedAccountError.isNotEmpty(),
                textStyle = MaterialTheme.typography.bodyMedium,
                label = { Hint(stringResource(R.string.transaction_editor_account_hint)) }
            )

            ExposedDropdownMenu(
                expanded = state.accountsDropdownIsExpanded,
                onDismissRequest = { handleViewAction(TransactionEditorViewAction.ManageAccountDropdown) }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.transaction_editor_account_add),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = { handleViewAction(TransactionEditorViewAction.CreateNewAccount) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                )

                state.accounts.forEach { account ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = account.name,
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
                    )
                }
            }
        }
        if (state.selectedAccountError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))

            Hint(value = state.selectedAccountError)
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
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Hint(stringResource(R.string.transaction_editor_category_hint)) }
        )

        ExposedDropdownMenu(
            expanded = state.categoriesDropdownIsExpanded,
            onDismissRequest = { handleViewAction(TransactionEditorViewAction.ManageCategoryDropdown) },
        ) {
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

            state.categories.forEach { category ->
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
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(color = Color(category.color).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(20.dp),
                                model = category.iconModel.icon,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = Color(category.color))
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun DateRow(
    modifier: Modifier = Modifier,
    state: TransactionEditorState,
    handleViewAction: (TransactionEditorViewAction) -> Unit
) {
    val context = LocalContext.current
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(state.date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        handleViewAction(TransactionEditorViewAction.ManageDateDialog)
                    }
                }
            },
        value = state.date.toFormatDate(context),
        onValueChange = {},
        readOnly = true,
        label = { Hint(stringResource(R.string.transaction_editor_date_hint)) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.transaction_editor_date_content_description)
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
    )

    if (state.dateDialogIsExpanded) {
        DatePickerModal(
            initialDate = state.date,
            onDateSelected = { handleViewAction(TransactionEditorViewAction.SaveDate(date = it)) },
            onDismiss = { handleViewAction(TransactionEditorViewAction.ManageDateDialog) },
        )
    }
}
