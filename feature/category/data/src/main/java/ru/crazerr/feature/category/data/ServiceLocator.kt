package ru.crazerr.feature.category.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.category.data.dataSource.CategoryLocalDataSource
import ru.crazerr.feature.category.data.repository.CategoryRepositoryImpl
import ru.crazerr.feature.category.domain.repository.CategoryRepository

val categoryDataModule = module {
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }
    single {
        CategoryLocalDataSource(categoriesDao = get<AppDatabase>().categoriesDao())
    }
}