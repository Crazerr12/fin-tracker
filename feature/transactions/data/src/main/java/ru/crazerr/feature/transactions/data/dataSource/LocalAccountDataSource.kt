package ru.crazerr.feature.transactions.data.dataSource

import kotlinx.coroutines.flow.first
import ru.crazerr.core.database.accounts.dao.AccountsDao
import ru.crazerr.feature.account.data.api.toAccount
import ru.crazerr.feature.account.domain.api.Account

internal class LocalAccountDataSource(
    private val accountsDao: AccountsDao,
) {
    suspend fun getAccounts(): Result<List<Account>> = try {
        val accounts = accountsDao.getAllAccounts().first()

        Result.success(accounts.map { it.toAccount() })
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}