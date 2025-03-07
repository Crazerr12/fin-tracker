package ru.crazerr.feature.main.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.main.data.dataSource.LocalAccountsDataSource
import ru.crazerr.feature.main.data.dataSource.LocalBalanceDataSource
import ru.crazerr.feature.main.data.repository.AccountRepositoryImpl
import ru.crazerr.feature.main.data.repository.BalanceRepositoryImpl
import ru.crazerr.feature.main.domain.repository.AccountRepository
import ru.crazerr.feature.main.domain.repository.BalanceRepository

val mainDataModule = module {
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::BalanceRepositoryImpl) { bind<BalanceRepository>() }

    single {
        LocalBalanceDataSource(transactionsDao = get<AppDatabase>().transactionsDao())
    }
    single {
        LocalAccountsDataSource(accountsDao = get<AppDatabase>().accountsDao())
    }

    includes(databaseModule)
}