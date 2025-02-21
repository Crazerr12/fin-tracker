package ru.crazerr.feature.account.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.Hint
import ru.crazerr.core.utils.components.LoadingView
import ru.crazerr.feature.account.presentation.AccountEditorComponent
import ru.crazerr.feature.account.presentation.AccountEditorState
import ru.crazerr.feature.account.presentation.AccountEditorViewAction
import ru.crazerr.feature.account.presentation.R
import ru.crazerr.feature.account.presentation.utils.AmountVisualTransformation

@Composable
fun AccountEditorView(modifier: Modifier = Modifier, component: AccountEditorComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        LoadingView()
    } else {
        AccountEditorContentView(
            modifier = modifier,
            state = state,
            handleViewAction = component::handleViewAction
        )
    }
}

@Composable
private fun AccountEditorContentView(
    modifier: Modifier = Modifier,
    state: AccountEditorState,
    handleViewAction: (AccountEditorViewAction) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        topBar = {
            AccountEditorTopBar(state = state, handleViewAction = handleViewAction)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            AccountEditorCard(state = state, handleViewAction = handleViewAction)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { handleViewAction(AccountEditorViewAction.SaveClick) },
                enabled = !state.buttonIsLoading
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
                    if (state.id != 0) R.string.account_editor_top_bar_edit_title
                    else R.string.account_editor_top_bar_create_title
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { handleViewAction(AccountEditorViewAction.BackClick) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.account_editor_back_button_content_description)
                )
            }
        }
    )
}

@Composable
private fun AccountEditorCard(
    modifier: Modifier = Modifier,
    state: AccountEditorState,
    handleViewAction: (AccountEditorViewAction) -> Unit,
) {
    Card(
        modifier = modifier
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
            visualTransformation = AmountVisualTransformation(sign = state.selectedCurrency.symbol[0]),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.selectedCurrency.name,
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
                readOnly = true,
                singleLine = true,
            )
        }
        DropdownMenu(
            expanded = state.isDropdownExpanded,
            onDismissRequest = { handleViewAction(AccountEditorViewAction.ManageDropdown) }
        ) {
            state.currencies.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        handleViewAction(
                            AccountEditorViewAction.SelectCurrency(
                                it
                            )
                        )
                    }
                )
            }
        }
    }
}