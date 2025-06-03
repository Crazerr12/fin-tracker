package ru.crazerr.feature.transactions.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.account.domain.api.Account

interface AccountRepository {
    fun getAccounts(): Result<Flow<List<Account>>>
}