package ru.crazerr.core.database.categories.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.crazerr.core.database.icons.model.IconEntity

data class CategoryWithIcon(
    @Embedded
    val categoryEntity: CategoryEntity,
    @Relation(entity = IconEntity::class, parentColumn = "icon_id", entityColumn = "id")
    val iconEntity: IconEntity,
)