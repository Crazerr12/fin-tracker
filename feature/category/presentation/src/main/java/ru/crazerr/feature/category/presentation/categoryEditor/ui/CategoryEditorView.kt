package ru.crazerr.feature.category.presentation.categoryEditor.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.components.Hint
import ru.crazerr.feature.category.presentation.R
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponent
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorState
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorViewAction
import ru.crazerr.feature.category.presentation.categoryEditor.InitialCategoryEditorState

private const val COLOR_ROW_SIZE = 6

@Composable
fun CategoryEditorView(modifier: Modifier = Modifier, component: CategoryEditorComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            CategoryEditorTopBar(state = state, handleViewAction = component::handleViewAction)
        },
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) { paddingValues ->
        CategoryEditorViewContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            handleViewAction = component::handleViewAction
        )
    }
}

@Composable
private fun CategoryEditorViewContent(
    modifier: Modifier = Modifier,
    state: CategoryEditorState,
    handleViewAction: (CategoryEditorViewAction) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.name,
                    onValueChange = { handleViewAction(CategoryEditorViewAction.UpdateName(it)) },
                    label = { Hint(stringResource(R.string.category_editor_name_hint)) },
                    singleLine = true,
                    supportingText = if (state.nameError.isNotEmpty()) {
                        { Hint(state.nameError) }
                    } else {
                        null
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    isError = state.nameError.isNotEmpty(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.category_editor_icon_hint),
                    style = MaterialTheme.typography.titleSmall,
                )

                Spacer(modifier = Modifier.height(8.dp))



                Spacer(modifier = Modifier.height(16.dp))

                ColorGrid(state = state, handleViewAction = handleViewAction)
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = { handleViewAction(CategoryEditorViewAction.SaveClick) }) {
            Text(
                text = stringResource(R.string.category_editor_save_button),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryEditorTopBar(
    modifier: Modifier = Modifier,
    state: CategoryEditorState,
    handleViewAction: (CategoryEditorViewAction) -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(
                    if (state.id != -1L) R.string.category_editor_top_bar_title_update
                    else R.string.category_editor_top_bar_title_create
                ),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = { handleViewAction(CategoryEditorViewAction.BackClick) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(ru.crazerr.core.utils.R.string.back_button_content_description),
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    )
}

@Composable
private fun ColorGrid(
    modifier: Modifier = Modifier,
    state: CategoryEditorState,
    handleViewAction: (CategoryEditorViewAction) -> Unit,
) {
    Text(
        text = stringResource(R.string.category_editor_color_hint),
        style = MaterialTheme.typography.titleSmall,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        state.colors.chunked(COLOR_ROW_SIZE).forEach { colors ->
            ColorRow(
                colors = colors,
                selectedColor = state.selectedColor,
                handleViewAction = handleViewAction,
            )
        }
    }
}

@Composable
private fun ColorRow(
    modifier: Modifier = Modifier,
    colors: List<Long>,
    selectedColor: Long,
    handleViewAction: (CategoryEditorViewAction) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        colors.forEach {
            ColorView(
                color = it,
                isSelected = selectedColor == it,
                onClick = { handleViewAction(CategoryEditorViewAction.UpdateColor(it)) },
            )
        }
    }
}

@Composable
private fun ColorView(
    modifier: Modifier = Modifier,
    color: Long,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(32.dp)
            .background(color = Color(color), shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}

@Preview()
@Composable
private fun ColorGridPreview() {
    Column { ColorGrid(state = InitialCategoryEditorState) { } }
}

@Preview
@Composable
private fun ColorRowPreview() {
    ColorRow(
        colors = InitialCategoryEditorState.colors.take(6),
        selectedColor = InitialCategoryEditorState.selectedColor
    ) { }
}

