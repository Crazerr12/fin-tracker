package ru.crazerr.feature.transaction.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.account.domain.api.Account

interface AccountRepository {
    suspend fun getAccounts(): Flow<Result<List<Account>>>
}