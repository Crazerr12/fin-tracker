package ru.crazerr.feature.transactions.data.dataSource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.accounts.dao.AccountsDao
import ru.crazerr.feature.account.data.api.toAccount
import ru.crazerr.feature.account.domain.api.Account

internal class LocalAccountDataSource(
    private val accountsDao: AccountsDao,
) {
    fun getAccounts(): Result<Flow<List<Account>>> = runCatching {
        accountsDao.getAllAccounts().map { it.map { it.toAccount() } }
    }
}