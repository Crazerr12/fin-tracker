package ru.crazerr.feature.main.presentation.main

import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.feature.main.domain.repository.AccountRepository
import ru.crazerr.feature.main.domain.repository.BalanceRepository

class MainDependencies(
    val accountRepository: AccountRepository,
    val balanceRepository: BalanceRepository,
    val resourceManager: ResourceManager,
)