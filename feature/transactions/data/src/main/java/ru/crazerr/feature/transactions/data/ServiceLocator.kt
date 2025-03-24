package ru.crazerr.feature.transactions.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.transactions.data.dataSource.LocalAccountDataSource
import ru.crazerr.feature.transactions.data.dataSource.LocalCategoryDataSource
import ru.crazerr.feature.transactions.data.dataSource.LocalTransactionsDataSource
import ru.crazerr.feature.transactions.data.repository.AccountRepositoryImpl
import ru.crazerr.feature.transactions.data.repository.CategoryRepositoryImpl
import ru.crazerr.feature.transactions.data.repository.TransactionRepositoryImpl
import ru.crazerr.feature.transactions.domain.repository.AccountRepository
import ru.crazerr.feature.transactions.domain.repository.CategoryRepository
import ru.crazerr.feature.transactions.domain.repository.TransactionRepository

val transactionsDataModule = module {
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>() }
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }

    singleOf(::LocalTransactionsDataSource)
    single {
        LocalAccountDataSource(accountsDao = get<AppDatabase>().accountsDao())
    }
    single {
        LocalCategoryDataSource(categoriesDao = get<AppDatabase>().categoriesDao())
    }

    includes(databaseModule)
}