package ru.crazerr.feature.main.data.dataSource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.crazerr.core.database.accounts.dao.AccountsDao
import ru.crazerr.feature.account.data.api.toAccount
import ru.crazerr.feature.account.domain.api.Account

internal class AccountLocalDataSource(
    private val accountsDao: AccountsDao,
) {
    suspend fun deleteAccountById(id: Long): Result<Long> = try {
        accountsDao.deleteAccountById(id = id)

        Result.success(id)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    fun getAllAccounts(): Flow<Result<List<Account>>> =
        accountsDao.getAllAccounts().map { entities ->
            try {
                Result.success(entities.map { it.toAccount() })
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
}