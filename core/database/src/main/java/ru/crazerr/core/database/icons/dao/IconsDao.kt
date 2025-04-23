package ru.crazerr.core.database.icons.dao

import androidx.room.Dao
import androidx.room.Query
import ru.crazerr.core.database.base.dao.BaseDao
import ru.crazerr.core.database.icons.model.IconEntity

@Dao
interface IconsDao : BaseDao<IconEntity> {

    @Query("SELECT * FROM icons WHERE purpose = :purpose")
    suspend fun getIconsByPurpose(purpose: String): List<IconEntity>
}