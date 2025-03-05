package ru.crazerr.feature.transaction.data.dataSource

import ru.crazerr.core.database.accounts.dao.AccountsDao
import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.transaction.data.model.toAccount

internal class LocalAccountDataSource(
    private val accountsDao: AccountsDao
) {
    suspend fun getAccounts(): Result<List<Account>> =
        try {
            val accountEntities = accountsDao.getAllAccounts()

            Result.success(accountEntities.map { it.toAccount() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
}