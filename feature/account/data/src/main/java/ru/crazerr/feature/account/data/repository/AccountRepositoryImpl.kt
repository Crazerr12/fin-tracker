package ru.crazerr.feature.account.data.repository

import ru.crazerr.feature.account.data.dataSource.AccountLocalDataSource
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.account.domain.repository.AccountRepository

internal class AccountRepositoryImpl(
    private val accountLocalDataSource: AccountLocalDataSource,
) : AccountRepository {
    override suspend fun createAccount(account: Account): Result<Account> =
        accountLocalDataSource.createAccount(account = account)

    override suspend fun updateAccount(account: Account): Result<Account> =
        accountLocalDataSource.updateAccount(account = account)

    override suspend fun getAccountById(id: Long): Result<Account> =
        accountLocalDataSource.getAccountById(id = id)
}