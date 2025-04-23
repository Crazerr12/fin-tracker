package ru.crazerr.core.root

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.crazerr.core.utils.snackbar.SnackbarManager
import ru.crazerr.feature.analysis.presentation.analysisStory.ui.AnalysisStoryCoordinator
import ru.crazerr.feature.budgets.presentation.budgetsStory.ui.BudgetsStoryCoordinator
import ru.crazerr.feature.main.presentation.mainStory.ui.MainStoryCoordinator
import ru.crazerr.feature.transactions.presentation.transactionsStory.ui.TransactionsStoryCoordinator

@Composable
fun RootCoordinator(modifier: Modifier = Modifier, component: RootComponent) {
    val stack by component.stack.subscribeAsState()
    val selectedBottomNavigationItem by component.selectedBottomNavigationItem.subscribeAsState()
    val bottomNavigation by component.bottomNavigation.subscribeAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            GlobalSnackbarHost(snackbarManager = component.snackbarManager)
        },
        bottomBar = {
            if (bottomNavigation) {
                BottomNavigationView(
                    selectedBottomNavigationItem = selectedBottomNavigationItem,
                    onNavigate = component::obtainBottomNavigation,
                )
            }
        },
        contentWindowInsets = if (bottomNavigation) WindowInsets(0) else WindowInsets.navigationBars,
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
                is RootComponent.Child.MainStory -> MainStoryCoordinator(component = child.component)
                is RootComponent.Child.TransactionsStory -> TransactionsStoryCoordinator(component = child.component)
                is RootComponent.Child.BudgetsStory -> BudgetsStoryCoordinator(component = child.component)
                is RootComponent.Child.AnalysisStory -> AnalysisStoryCoordinator(component = child.component)
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
                actionLabel = data.actionLabel,
                duration = SnackbarDuration.Short
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
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(it.iconRes),
                    contentDescription = null,
                    tint = if (selectedBottomNavigationItem == it) MaterialTheme.colorScheme.primary else LocalContentColor.current
                )

//                Spacer(modifier = Modifier.height(2.dp))
//
//                Text(
//                    text = stringResource(it.stringRes),
//                    style = MaterialTheme.typography.titleSmall.copy(
//                        color = if (selectedBottomNavigationItem == it) MaterialTheme.colorScheme.primary else LocalContentColor.current
//                    ),
//                )
            }
        }
    }
}