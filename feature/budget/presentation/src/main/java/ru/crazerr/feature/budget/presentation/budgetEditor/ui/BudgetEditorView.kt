package ru.crazerr.feature.budget.presentation.budgetEditor.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.Hint
import ru.crazerr.core.utils.presentation.toAmountFormat
import ru.crazerr.core.utils.visualTransformations.AmountVisualTransformation
import ru.crazerr.feature.budget.presentation.R
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorComponent
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorState
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorViewAction
import java.time.LocalDate
import ru.crazerr.core.utils.R as utilsR

@Composable
fun BudgetEditorView(modifier: Modifier = Modifier, component: BudgetEditorComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BudgetEditorTopBar(state = state, onBackClick = {
                component.handleViewAction(
                    BudgetEditorViewAction.BackClick
                )
            })
        },
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) { paddingValues ->
        BudgetEditorViewContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            handleViewAction = component::handleViewAction
        )
    }
}

@Composable
private fun BudgetEditorViewContent(
    modifier: Modifier = Modifier,
    state: BudgetEditorState,
    handleViewAction: (BudgetEditorViewAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        BudgetCard(state = state, handleViewAction = handleViewAction)

        if (state.date.month == LocalDate.now().month && state.date.year == LocalDate.now().year) {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = !state.buttonIsLoading,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { handleViewAction(BudgetEditorViewAction.SaveClick) },
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(utilsR.string.save_button),
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
private fun BudgetEditorTopBar(
    modifier: Modifier = Modifier,
    state: BudgetEditorState,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(if (state.id != -1L) R.string.budget_editor_update_title else R.string.budget_editor_create_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(utilsR.string.back_button_content_description),
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    )
}

@Composable
private fun BudgetCard(
    modifier: Modifier = Modifier,
    state: BudgetEditorState,
    handleViewAction: (BudgetEditorViewAction) -> Unit,
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
                .padding(16.dp),
        ) {
            CategoryDropdown(state = state, handleViewAction = handleViewAction)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.maxAmount,
                onValueChange = { handleViewAction(BudgetEditorViewAction.UpdateMaxAmount(it)) },
                label = { Hint(value = stringResource(R.string.budget_editor_amount_hint)) },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                isError = state.maxAmountError.isNotEmpty(),
                visualTransformation = AmountVisualTransformation(sign = '₽'),
                supportingText = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = stringResource(R.string.budget_editor_current_amount_supporting_text),
                                style = MaterialTheme.typography.bodySmall,
                            )

                            Text(
                                text = state.currentAmount.toAmountFormat('₽'),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }

                        if (state.maxAmountError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))

                            Hint(
                                value = state.maxAmountError,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            CheckboxRow(
                isChecked = state.isRegular,
                onCheck = { handleViewAction(BudgetEditorViewAction.CheckIsRegular) },
                title = stringResource(R.string.budget_editor_is_regular_budget),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.budget_editor_notifications),
                style = MaterialTheme.typography.titleSmall,
            )

            Spacer(modifier = Modifier.height(4.dp))

            CheckboxRow(
                isChecked = state.isWarning,
                onCheck = { handleViewAction(BudgetEditorViewAction.CheckWarningBudgetClose) },
                title = stringResource(R.string.budget_editor_budget_closed_to_70),
            )

            Spacer(modifier = Modifier.height(4.dp))

            CheckboxRow(
                isChecked = state.isAlarm,
                onCheck = { handleViewAction(BudgetEditorViewAction.CheckAlarmBudgetExceeded) },
                title = stringResource(R.string.budget_editor_budget_exceeded),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    modifier: Modifier = Modifier,
    state: BudgetEditorState,
    handleViewAction: (BudgetEditorViewAction) -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = state.categoriesDropdownIsExpand,
        onExpandedChange = { handleViewAction(BudgetEditorViewAction.ManageCategoriesDropdown) },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = state.selectedCategory.name,
            onValueChange = {},
            isError = state.selectedCategoryError.isNotEmpty(),
            supportingText = if (state.selectedCategoryError.isNotEmpty()) {
                { Hint(state.selectedCategoryError) }
            } else null,
            readOnly = true,
            singleLine = true,
            label = { Hint(value = stringResource(R.string.budget_editor_expenses_category_hint)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.categoriesDropdownIsExpand) },
            textStyle = MaterialTheme.typography.bodyMedium,
        )

        ExposedDropdownMenu(
            expanded = state.categoriesDropdownIsExpand,
            onDismissRequest = { handleViewAction(BudgetEditorViewAction.ManageCategoriesDropdown) },
        ) {
            state.categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = {
                        handleViewAction(
                            BudgetEditorViewAction.UpdateSelectedCategory(
                                category = category
                            )
                        )
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(
                                    color = Color(category.color).copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            AsyncImage(
                                modifier = Modifier.size(20.dp),
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
private fun CheckboxRow(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheck: () -> Unit,
    title: String,
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onCheck()
            }
        }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                checkForNotificationPermission(
                    launcher = launcher,
                    context = context,
                    action = onCheck
                )
            })

        Text(text = title, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun checkForNotificationPermission(
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    context: Context,
    action: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> action()

            else -> launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    } else {
        action()
    }
}