package ru.crazerr.feature.transaction.data.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.transaction.data.dataSource.AccountLocalDataSource
import ru.crazerr.feature.transaction.domain.repository.AccountRepository

internal class AccountRepositoryImpl(
    private val accountLocalDataSource: AccountLocalDataSource,
) : AccountRepository {
    override suspend fun getAccounts(): Flow<Result<List<Account>>> {
        return accountLocalDataSource.getAccounts()
    }
}