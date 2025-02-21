package ru.crazerr.feature.account.presentation

import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.account.domain.repository.AccountRepository
import ru.crazerr.feature.account.domain.repository.CurrencyRepository

data class AccountEditorDependencies(
    val accountRepository: AccountRepository,
    val currencyRepository: CurrencyRepository,
    val resourceManager: ResourceManager,
    val args: AccountEditorArgs,
)