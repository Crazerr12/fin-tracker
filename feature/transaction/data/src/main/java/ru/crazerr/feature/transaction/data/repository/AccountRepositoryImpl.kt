package ru.crazerr.feature.transaction.data.repository

import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.transaction.data.dataSource.LocalAccountDataSource
import ru.crazerr.feature.transaction.domain.AccountRepository

internal class AccountRepositoryImpl(
    private val localAccountDataSource: LocalAccountDataSource,
) : AccountRepository {
    override suspend fun getAccounts(): Result<List<Account>> {
        return localAccountDataSource.getAccounts()
    }
}