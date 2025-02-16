package ru.crazerr.core.database.accounts.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.base.dao.BaseDao

@Dao
interface AccountsDao : BaseDao<AccountEntity> {

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<AccountEntity>
}