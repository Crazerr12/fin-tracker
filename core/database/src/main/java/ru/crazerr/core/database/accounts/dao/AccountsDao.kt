package ru.crazerr.core.database.accounts.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.accounts.model.AccountWithCurrency
import ru.crazerr.core.database.base.dao.BaseDao

@Dao
interface AccountsDao : BaseDao<AccountEntity> {

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<AccountEntity>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountWithCurrency
}