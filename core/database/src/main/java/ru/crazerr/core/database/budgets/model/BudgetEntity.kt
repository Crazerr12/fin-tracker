package ru.crazerr.core.database.budgets.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.crazerr.core.database.categories.model.CategoryEntity
import java.time.LocalDate

@Entity(
    tableName = "budgets",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
    )],
    indices = [Index("category_id")]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "max_amount") val maxAmount: Long,
    @ColumnInfo(name = "current_amount") val currentAmount: Long,
    @ColumnInfo(name = "is_regular") val isRegular: Boolean,
    val date: LocalDate,
    )
