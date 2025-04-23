package ru.crazerr.core.utils.navigation

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class NavigationManagerComponent : KoinComponent {
    internal val navigationManager: NavigationManager by inject()
}

fun navigationManager(): NavigationManager = NavigationManagerComponent().navigationManager