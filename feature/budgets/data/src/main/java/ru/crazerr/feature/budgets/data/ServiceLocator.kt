package ru.crazerr.feature.budgets.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.budgets.data.dataSource.BudgetLocalDataSource
import ru.crazerr.feature.budgets.data.repository.BudgetRepositoryImpl
import ru.crazerr.feature.budgets.domain.repository.BudgetRepository

val budgetsDataModule = module {
    singleOf(::BudgetRepositoryImpl) { bind<BudgetRepository>() }
    singleOf(::BudgetLocalDataSource)

    includes(databaseModule)
}