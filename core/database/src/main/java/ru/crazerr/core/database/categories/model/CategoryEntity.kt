package ru.crazerr.core.database.categories.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: Long,
    @ColumnInfo(name = "icon_id") val iconId: String,
    @ColumnInfo(name = "is_template") val isTemplate: Boolean,
)
