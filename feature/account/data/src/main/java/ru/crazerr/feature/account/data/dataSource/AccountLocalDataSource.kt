package ru.crazerr.feature.account.data.dataSource

import ru.crazerr.core.database.accounts.dao.AccountsDao
import ru.crazerr.feature.account.data.models.toAccount
import ru.crazerr.feature.account.data.models.toAccountEntity
import ru.crazerr.feature.account.domain.api.Account

internal class AccountLocalDataSource(
    private val accountsDao: AccountsDao,
) {
    suspend fun createAccount(account: Account): Result<Account> =
        try {
            val idList = accountsDao.insert(account.toAccountEntity())

            Result.success(account.copy(id = idList[0].toInt()))
        } catch (ex: Exception) {
            Result.failure(ex)
        }


    suspend fun updateAccount(account: Account): Result<Account> =
        try {
            accountsDao.update(account.toAccountEntity())

            Result.success(account)
        } catch (ex: Exception) {
            Result.failure(ex)
        }

    suspend fun getAccountById(id: Int): Result<Account> =
        try {
            val accountEntity = accountsDao.getAccountById(id = id)

            Result.success(accountEntity.toAccount())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
}