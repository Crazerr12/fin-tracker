package ru.crazerr.core.root

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.crazerr.core.utils.snackbar.SnackbarManager

@Composable
fun RootCoordinator(modifier: Modifier = Modifier, component: RootComponent) {
    val stack by component.stack.subscribeAsState()
    val selectedBottomNavigationItem by component.selectedBottomNavigationItem.subscribeAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            GlobalSnackbarHost(snackbarManager = component.snackbarManager)
        },
        bottomBar = {
            if (component.isBottomNavigationVisible()) {
                BottomNavigationView(
                    selectedBottomNavigationItem = selectedBottomNavigationItem,
                    onNavigate = component::obtainBottomNavigation
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) { paddingValues ->
        Children(
            stack = stack,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            animation = stackAnimation()
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.AuthStory -> Box() {}
                is RootComponent.Child.MainStory -> Box() {}
                is RootComponent.Child.TransactionsStory -> Box() {}
                is RootComponent.Child.BudgetStory -> Box() {}
                is RootComponent.Child.AnalysisStory -> Box() {}
                is RootComponent.Child.ProfileStory -> Box() {}
            }
        }
    }
}

@Composable
private fun GlobalSnackbarHost(snackbarManager: SnackbarManager) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData by snackbarManager.snackbarState.collectAsState()

    LaunchedEffect(snackbarData) {
        snackbarData?.let { data ->
            val result = snackbarHostState.showSnackbar(
                message = data.message,
                actionLabel = data.actionLabel
            )

            if (result == SnackbarResult.ActionPerformed) {
                data.onAction?.invoke()
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState, modifier = Modifier.imePadding())
}

@Composable
private fun BottomNavigationView(
    modifier: Modifier = Modifier,
    selectedBottomNavigationItem: BottomNavigationItem,
    onNavigate: (BottomNavigationItem) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        BottomNavigationItem.entries.forEach {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = { onNavigate(it) })
                    .padding(top = 6.dp, bottom = 2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(it.iconRes),
                    contentDescription = null,
                    tint = if (selectedBottomNavigationItem == it) Color.Blue else LocalContentColor.current
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(it.stringRes),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (selectedBottomNavigationItem == it) Color.Blue else LocalContentColor.current
                    ),
                )
            }
        }
    }
}