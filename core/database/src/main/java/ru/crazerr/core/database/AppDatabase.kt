package ru.crazerr.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.crazerr.core.database.accounts.dao.AccountsDao
import ru.crazerr.core.database.accounts.model.AccountEntity
import ru.crazerr.core.database.budgets.dao.BudgetsDao
import ru.crazerr.core.database.budgets.dao.RepeatBudgetsDao
import ru.crazerr.core.database.budgets.model.BudgetEntity
import ru.crazerr.core.database.budgets.model.RepeatBudgetEntity
import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.core.database.categories.model.CategoryEntity
import ru.crazerr.core.database.converters.IntListConverter
import ru.crazerr.core.database.converters.LocalDateConverter
import ru.crazerr.core.database.currencies.dao.CurrenciesDao
import ru.crazerr.core.database.currencies.model.CurrencyEntity
import ru.crazerr.core.database.icons.model.IconEntity
import ru.crazerr.core.database.repeatTransactions.dao.RepeatTransactionsDao
import ru.crazerr.core.database.repeatTransactions.model.RepeatTransactionEntity
import ru.crazerr.core.database.transactions.dao.TransactionsDao
import ru.crazerr.core.database.transactions.model.TransactionEntity

private const val CURRENT_DATABASE_VERSION = 1

@Database(
    entities = [
        TransactionEntity::class,
        RepeatTransactionEntity::class,
        CurrencyEntity::class,
        AccountEntity::class,
        BudgetEntity::class,
        CategoryEntity::class,
        IconEntity::class,
        RepeatBudgetEntity::class,
    ],
    version = CURRENT_DATABASE_VERSION,
    exportSchema = false,
)
@TypeConverters(LocalDateConverter::class, IntListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionsDao(): TransactionsDao

    abstract fun repeatTransactionsDao(): RepeatTransactionsDao

    abstract fun currenciesDao(): CurrenciesDao

    abstract fun accountsDao(): AccountsDao

    abstract fun budgetsDao(): BudgetsDao

    abstract fun categoriesDao(): CategoriesDao

    abstract fun repeatBudgetsDao(): RepeatBudgetsDao
}