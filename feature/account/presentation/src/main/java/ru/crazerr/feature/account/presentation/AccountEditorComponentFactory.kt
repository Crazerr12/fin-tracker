package ru.crazerr.feature.account.presentation

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.account.domain.repository.AccountRepository
import ru.crazerr.feature.account.domain.repository.CurrencyRepository
import ru.crazerr.feature.account.domain.repository.IconRepository

interface AccountEditorComponentFactory {
    fun create(
        componentContext: ComponentContext,
        onAction: (AccountEditorComponentAction) -> Unit,
        args: AccountEditorArgs,
    ): AccountEditorComponent
}

internal class AccountEditorComponentFactoryImpl(
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository,
    private val iconRepository: IconRepository,
    private val resourceManager: ResourceManager,
) : AccountEditorComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (AccountEditorComponentAction) -> Unit,
        args: AccountEditorArgs,
    ): AccountEditorComponent = AccountEditorComponentImpl(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = AccountEditorDependencies(
            accountRepository = accountRepository,
            currencyRepository = currencyRepository,
            resourceManager = resourceManager,
            iconRepository = iconRepository,
            args = args,
        )
    )
}