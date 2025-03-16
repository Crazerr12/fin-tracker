package ru.crazerr.core.database.categories.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.crazerr.core.database.icons.model.IconEntity

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = IconEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("icon_id"),
    )],
    indices = [Index("icon_id")]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: Long,
    @ColumnInfo(name = "icon_id") val iconId: Int,
    @ColumnInfo(name = "is_template") val isTemplate: Boolean,
)
