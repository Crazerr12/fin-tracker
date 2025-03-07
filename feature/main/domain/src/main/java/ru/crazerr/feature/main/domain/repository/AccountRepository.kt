package ru.crazerr.feature.main.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.account.domain.api.Account

interface AccountRepository {
    suspend fun getAccounts(): Flow<Result<List<Account>>>

    suspend fun deleteAccountById(id: Int): Result<Int>
}