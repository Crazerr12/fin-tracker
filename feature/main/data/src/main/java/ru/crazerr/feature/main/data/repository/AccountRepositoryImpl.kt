package ru.crazerr.feature.main.data.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.main.data.dataSource.LocalAccountsDataSource
import ru.crazerr.feature.main.domain.repository.AccountRepository

internal class AccountRepositoryImpl(
    private val localAccountsDataSource: LocalAccountsDataSource,
) : AccountRepository {
    override suspend fun getAccounts(): Flow<Result<List<Account>>> {
        return localAccountsDataSource.getAllAccounts()
    }

    override suspend fun deleteAccountById(id: Int): Result<Int> {
        return localAccountsDataSource.deleteAccountById(id = id)
    }
}