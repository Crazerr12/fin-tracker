package ru.crazerr.feature.budget.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.budget.data.dataSource.BudgetLocalDataSource
import ru.crazerr.feature.budget.data.dataSource.CategoryLocalDataSource
import ru.crazerr.feature.budget.data.dataSource.TransactionLocalDataSource
import ru.crazerr.feature.budget.data.repository.BudgetRepositoryImpl
import ru.crazerr.feature.budget.data.repository.CategoryRepositoryImpl
import ru.crazerr.feature.budget.data.repository.TransactionRepositoryImpl
import ru.crazerr.feature.budget.data.workManager.BudgetManager
import ru.crazerr.feature.budget.domain.BudgetRepository
import ru.crazerr.feature.budget.domain.CategoryRepository
import ru.crazerr.feature.budget.domain.TransactionRepository

val budgetDataModule = module {
    singleOf(::BudgetRepositoryImpl) { bind<BudgetRepository>() }
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }

    singleOf(::BudgetLocalDataSource)
    single {
        TransactionLocalDataSource(transactionsDao = get<AppDatabase>().transactionsDao())
    }
    single {
        CategoryLocalDataSource(categoriesDao = get<AppDatabase>().categoriesDao())
    }

    singleOf(::BudgetManager)

    includes(databaseModule)
}