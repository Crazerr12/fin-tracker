package ru.crazerr.feature.account.presentation

import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.account.domain.repository.AccountRepository
import ru.crazerr.feature.account.domain.repository.CurrencyRepository
import ru.crazerr.feature.account.domain.repository.IconRepository

data class AccountEditorDependencies(
    val accountRepository: AccountRepository,
    val currencyRepository: CurrencyRepository,
    val iconRepository: IconRepository,
    val resourceManager: ResourceManager,
    val args: AccountEditorArgs,
)