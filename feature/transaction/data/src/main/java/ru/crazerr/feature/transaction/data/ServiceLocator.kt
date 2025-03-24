package ru.crazerr.feature.transaction.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.transaction.data.dataSource.AccountLocalDataSource
import ru.crazerr.feature.transaction.data.dataSource.CategoryLocalDataSource
import ru.crazerr.feature.transaction.data.dataSource.TransactionLocalDataSource
import ru.crazerr.feature.transaction.data.repository.AccountRepositoryImpl
import ru.crazerr.feature.transaction.data.repository.CategoryRepositoryImpl
import ru.crazerr.feature.transaction.data.repository.TransactionRepositoryImpl
import ru.crazerr.feature.transaction.domain.repository.AccountRepository
import ru.crazerr.feature.transaction.domain.repository.CategoryRepository
import ru.crazerr.feature.transaction.domain.repository.TransactionRepository

val transactionDataModule = module {
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>() }
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }

    single {
        TransactionLocalDataSource(appDatabase = get())
    }
    single {
        AccountLocalDataSource(accountsDao = get<AppDatabase>().accountsDao())
    }
    single {
        CategoryLocalDataSource(categoryDao = get<AppDatabase>().categoriesDao())
    }
}