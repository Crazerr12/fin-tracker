package ru.crazerr.feature.main.presentation.main

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.presentation.ComponentFactory
import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.main.domain.repository.AccountRepository
import ru.crazerr.feature.main.domain.repository.BalanceRepository

interface MainComponentFactory : ComponentFactory<MainComponent, MainComponentAction>

internal class MainComponentFactoryImpl(
    private val balanceRepository: BalanceRepository,
    private val accountRepository: AccountRepository,
    private val resourceManager: ResourceManager,
) : MainComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (MainComponentAction) -> Unit
    ): MainComponent = MainComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = MainDependencies(
            accountRepository = accountRepository,
            balanceRepository = balanceRepository,
            resourceManager = resourceManager
        )
    )
}