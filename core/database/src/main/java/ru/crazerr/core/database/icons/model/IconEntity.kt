package ru.crazerr.core.database.icons.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "icons")
data class IconEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val icon: ByteArray,
)