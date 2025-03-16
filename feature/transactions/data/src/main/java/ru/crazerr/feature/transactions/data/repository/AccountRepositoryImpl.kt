package ru.crazerr.feature.transactions.data.repository

import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.transactions.data.dataSource.LocalAccountDataSource
import ru.crazerr.feature.transactions.domain.repository.AccountRepository

internal class AccountRepositoryImpl(
    private val localAccountDataSource: LocalAccountDataSource
) : AccountRepository {
    override suspend fun getAccounts(): Result<List<Account>> = localAccountDataSource.getAccounts()
}