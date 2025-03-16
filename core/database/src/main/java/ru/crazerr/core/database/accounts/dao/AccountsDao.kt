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

    @Transaction
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountWithCurrency>>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountWithCurrency

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccountById(id: Int)

    @Query("UPDATE accounts SET amount = amount + :transactionAmount WHERE id = :id ")
    suspend fun updateAccountBalance(id: Int, transactionAmount: Long): Int
}