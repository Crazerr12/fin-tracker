package ru.crazerr.core.database.transactions.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.accounts.model.AccountWithCurrency
import ru.crazerr.core.database.categories.model.CategoryEntity
import ru.crazerr.core.database.categories.model.CategoryWithIcon

data class TransactionWithAccountAndCategory(
    @Embedded val transaction: TransactionEntity,
    @Relation(entity = CategoryEntity::class, parentColumn = "category_id", entityColumn = "id")
    val category: CategoryWithIcon,
    @Relation(entity = AccountEntity::class, parentColumn = "account_id", entityColumn = "id")
    val accountWithCurrency: AccountWithCurrency
)
