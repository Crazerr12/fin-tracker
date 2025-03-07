package ru.crazerr.feature.transaction.data.dataSource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.accounts.dao.AccountsDao
import ru.crazerr.feature.account.data.api.toAccount
import ru.crazerr.feature.account.domain.api.Account

internal class LocalAccountDataSource(
    private val accountsDao: AccountsDao
) {
    fun getAccounts(): Flow<Result<List<Account>>> = accountsDao.getAllAccounts().map { entities ->
        try {
            Result.success(entities.map { it.toAccount() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}