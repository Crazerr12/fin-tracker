package ru.crazerr.feature.transaction.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.transaction.data.dataSource.LocalAccountDataSource
import ru.crazerr.feature.transaction.data.dataSource.LocalCategoryDataSource
import ru.crazerr.feature.transaction.data.dataSource.LocalTransactionDataSource
import ru.crazerr.feature.transaction.data.repository.AccountRepositoryImpl
import ru.crazerr.feature.transaction.data.repository.CategoryRepositoryImpl
import ru.crazerr.feature.transaction.data.repository.TransactionRepositoryImpl
import ru.crazerr.feature.transaction.domain.AccountRepository
import ru.crazerr.feature.transaction.domain.CategoryRepository
import ru.crazerr.feature.transaction.domain.TransactionRepository

val transactionDataModule = module {
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>() }
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }

    single {
        LocalTransactionDataSource(appDatabase = get())
    }
    single {
        LocalAccountDataSource(accountsDao = get<AppDatabase>().accountsDao())
    }
    single {
        LocalCategoryDataSource(categoryDao = get<AppDatabase>().categoriesDao())
    }
}