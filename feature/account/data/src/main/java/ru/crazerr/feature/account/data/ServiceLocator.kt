package ru.crazerr.feature.account.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.account.data.dataSource.AccountLocalDataSource
import ru.crazerr.feature.account.data.dataSource.CurrencyLocalDataSource
import ru.crazerr.feature.account.data.dataSource.IconLocalDataSource
import ru.crazerr.feature.account.data.repository.AccountRepositoryImpl
import ru.crazerr.feature.account.data.repository.CurrencyRepositoryImpl
import ru.crazerr.feature.account.data.repository.IconRepositoryImpl
import ru.crazerr.feature.account.domain.repository.AccountRepository
import ru.crazerr.feature.account.domain.repository.CurrencyRepository
import ru.crazerr.feature.account.domain.repository.IconRepository

val accountDataModule = module {
    single {
        AccountLocalDataSource(accountsDao = get<AppDatabase>().accountsDao())
    }
    single {
        CurrencyLocalDataSource(currenciesDao = get<AppDatabase>().currenciesDao())
    }
    singleOf(::IconLocalDataSource)

    singleOf(::IconRepositoryImpl) { bind<IconRepository>() }
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::CurrencyRepositoryImpl) { bind<CurrencyRepository>() }

    includes(databaseModule)
}