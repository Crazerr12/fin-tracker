package ru.crazerr.feature.settings.presentation.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.feature.settings.presentation.R
import ru.crazerr.feature.settings.presentation.settings.SettingsComponent
import ru.crazerr.feature.settings.presentation.settings.SettingsState
import ru.crazerr.feature.settings.presentation.settings.SettingsViewAction
import ru.crazerr.core.utils.R as utilsR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(modifier: Modifier = Modifier, component: SettingsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_top_bar_title),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.handleViewAction(SettingsViewAction.BackClick) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(utilsR.string.back_button_content_description),
                        )
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        SettingsContentView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            handleViewAction = component::handleViewAction,
            state = state,
        )
    }
}

@Composable
fun SettingsContentView(
    modifier: Modifier = Modifier,
    handleViewAction: (SettingsViewAction) -> Unit,
    state: SettingsState,
) {
    Column(modifier = modifier) {
        Param(
            name = stringResource(R.string.categories_top_bar_title),
            onClick = { handleViewAction(SettingsViewAction.CategoriesClick) },
        )
    }
}

@Composable
fun Param(modifier: Modifier = Modifier, name: String, onClick: () -> Unit) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                )
            }

            HorizontalDivider()
        }
    }
}