package ru.crazerr.feature.transactions.data.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.transactions.data.dataSource.LocalAccountDataSource
import ru.crazerr.feature.transactions.domain.repository.AccountRepository

internal class AccountRepositoryImpl(
    private val localAccountDataSource: LocalAccountDataSource
) : AccountRepository {
    override fun getAccounts(): Result<Flow<List<Account>>> =
        localAccountDataSource.getAccounts()
}