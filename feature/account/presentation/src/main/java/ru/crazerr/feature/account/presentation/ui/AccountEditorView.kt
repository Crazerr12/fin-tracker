package ru.crazerr.feature.account.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.Hint
import ru.crazerr.core.utils.components.LoadingView
import ru.crazerr.core.utils.components.incomeColor
import ru.crazerr.core.utils.presentation.conditional
import ru.crazerr.core.utils.visualTransformations.AmountVisualTransformation
import ru.crazerr.feature.account.presentation.AccountEditorComponent
import ru.crazerr.feature.account.presentation.AccountEditorState
import ru.crazerr.feature.account.presentation.AccountEditorViewAction
import ru.crazerr.feature.account.presentation.R
import ru.crazerr.feature.icon.domain.api.IconModel
import kotlin.collections.chunked
import kotlin.collections.forEach

private const val ICON_ROW_SIZE = 6

@Composable
fun AccountEditorView(modifier: Modifier = Modifier, component: AccountEditorComponent) {
    val state by component.state.subscribeAsState()

    AccountEditorContentView(
        modifier = modifier,
        state = state,
        handleViewAction = component::handleViewAction
    )
}

@Composable
private fun AccountEditorContentView(
    modifier: Modifier = Modifier,
    state: AccountEditorState,
    handleViewAction: (AccountEditorViewAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            AccountEditorTopBar(state = state, handleViewAction = handleViewAction)
        },
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingView(modifier = Modifier.padding(paddingValues))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(16.dp),
            ) {
                AccountEditorCard(state = state, handleViewAction = handleViewAction)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { handleViewAction(AccountEditorViewAction.SaveClick) },
                    enabled = !state.buttonIsLoading,
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.account_editor_button_save),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.alpha(if (state.buttonIsLoading) 0f else 1f)
                        )

                        if (state.buttonIsLoading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountEditorTopBar(
    state: AccountEditorState,
    handleViewAction: (AccountEditorViewAction) -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(
                    if (state.id != -1L) R.string.account_editor_top_bar_edit_title
                    else R.string.account_editor_top_bar_create_title
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { handleViewAction(AccountEditorViewAction.BackClick) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(ru.crazerr.core.utils.R.string.back_button_content_description)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
private fun AccountEditorCard(
    modifier: Modifier = Modifier,
    state: AccountEditorState,
    handleViewAction: (AccountEditorViewAction) -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                onValueChange = { handleViewAction(AccountEditorViewAction.UpdateName(name = it)) },
                label = { Hint(value = stringResource(R.string.account_editor_name_field_hint)) },
                singleLine = true,
                supportingText = if (state.nameError.isNotEmpty()) {
                    { Hint(value = state.nameError) }
                } else {
                    null
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                isError = state.nameError.isNotEmpty(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.amount,
                onValueChange = {
                    handleViewAction(
                        AccountEditorViewAction.UpdateCurrentAmount(
                            amount = it
                        )
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                label = { Hint(value = stringResource(R.string.account_editor_amount_field_hint)) },
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
                    sign = state.selectedCurrency.symbol.getOrElse(
                        index = 0,
                        defaultValue = { Char.MIN_VALUE })
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            IconGrid(state = state, handleViewAction = handleViewAction)

            Spacer(modifier = Modifier.height(16.dp))

            CurrencyTextField(state = state, handleViewAction = handleViewAction)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyTextField(
    modifier: Modifier = Modifier,
    state: AccountEditorState,
    handleViewAction: (AccountEditorViewAction) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = state.isDropdownExpanded,
        onExpandedChange = { handleViewAction(AccountEditorViewAction.ManageDropdown) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = "${state.selectedCurrency.code} - ${state.selectedCurrency.name}",
            onValueChange = {},
            label = { Hint(value = stringResource(R.string.account_editor_currency_field_hint)) },
            trailingIcon = {
                Icon(
                    imageVector = if (state.isDropdownExpanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = stringResource(
                        if (state.isDropdownExpanded) R.string.account_editor_currency_up_content_description
                        else R.string.account_editor_currency_down_content_description
                    )
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            readOnly = true,
            singleLine = true,
        )

        ExposedDropdownMenu(
            expanded = state.isDropdownExpanded,
            onDismissRequest = { handleViewAction(AccountEditorViewAction.ManageDropdown) }
        ) {
            state.currencies.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${it.code} - ${it.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        handleViewAction(
                            AccountEditorViewAction.SelectCurrency(
                                it
                            )
                        )
                    },
                    trailingIcon = {
                        Text(
                            text = it.symbol,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun IconGrid(
    modifier: Modifier = Modifier,
    state: AccountEditorState,
    handleViewAction: (AccountEditorViewAction) -> Unit,
) {
    Text(
        text = stringResource(R.string.account_editor_icon_hint),
        style = MaterialTheme.typography.titleSmall,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        state.icons.chunked(ICON_ROW_SIZE).forEach { icons ->
            IconRow(
                icons = icons,
                selectedIcon = state.selectedIcon,
                handleViewAction = handleViewAction,
            )
        }
    }
}


@Composable
private fun IconRow(
    modifier: Modifier = Modifier,
    icons: List<IconModel>,
    selectedIcon: IconModel,
    handleViewAction: (AccountEditorViewAction) -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        icons.forEach {
            IconView(
                icon = it,
                isSelected = selectedIcon.id == it.id,
                onClick = { handleViewAction(AccountEditorViewAction.SelectIcon(it)) },
            )
        }
    }
}

@Composable
private fun IconView(
    modifier: Modifier = Modifier,
    icon: IconModel,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(32.dp)
            .clickable(onClick = onClick)
            .conditional(
                condition = isSelected,
                ifTrue = { border(width = 2.dp, color = incomeColor, shape = CircleShape) }),
        contentAlignment = Alignment.Center,
    ) {
        if (icon.id == 21L || icon.id == 22L) {
            AsyncImage(
                model = icon.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
            )
        } else {
            AsyncImage(
                model = icon.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}