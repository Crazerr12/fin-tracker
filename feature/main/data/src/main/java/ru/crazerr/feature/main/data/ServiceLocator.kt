package ru.crazerr.feature.main.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.main.data.dataSource.AccountLocalDataSource
import ru.crazerr.feature.main.data.dataSource.BalanceLocalDataSource
import ru.crazerr.feature.main.data.repository.AccountRepositoryImpl
import ru.crazerr.feature.main.data.repository.BalanceRepositoryImpl
import ru.crazerr.feature.main.domain.repository.AccountRepository
import ru.crazerr.feature.main.domain.repository.BalanceRepository

val mainDataModule = module {
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::BalanceRepositoryImpl) { bind<BalanceRepository>() }

    single {
        BalanceLocalDataSource(transactionsDao = get<AppDatabase>().transactionsDao())
    }
    single {
        AccountLocalDataSource(accountsDao = get<AppDatabase>().accountsDao())
    }

    includes(databaseModule)
}