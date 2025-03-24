package ru.crazerr.feature.account.domain.repository

import ru.crazerr.feature.account.domain.api.Account

interface AccountRepository {
    suspend fun createAccount(account: Account): Result<Account>

    suspend fun updateAccount(account: Account): Result<Account>

    suspend fun getAccountById(id: Long): Result<Account>
}