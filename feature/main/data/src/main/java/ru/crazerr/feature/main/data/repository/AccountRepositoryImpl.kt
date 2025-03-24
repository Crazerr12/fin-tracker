package ru.crazerr.feature.main.data.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.main.data.dataSource.AccountLocalDataSource
import ru.crazerr.feature.main.domain.repository.AccountRepository

internal class AccountRepositoryImpl(
    private val accountLocalDataSource: AccountLocalDataSource,
) : AccountRepository {
    override suspend fun getAccounts(): Flow<Result<List<Account>>> {
        return accountLocalDataSource.getAllAccounts()
    }

    override suspend fun deleteAccountById(id: Long): Result<Long> {
        return accountLocalDataSource.deleteAccountById(id = id)
    }
}