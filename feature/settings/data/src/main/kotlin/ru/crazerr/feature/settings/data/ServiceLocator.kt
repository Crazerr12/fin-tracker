package ru.crazerr.feature.settings.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.settings.data.dataSource.LocalCategoryDataSource
import ru.crazerr.feature.settings.data.repository.CategoryRepositoryImpl
import ru.crazerr.feature.settings.domain.repository.CategoryRepository

val settingsDataModule = module {
    single {
        LocalCategoryDataSource(categoriesDao = get<AppDatabase>().categoriesDao())
    }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }

    includes(databaseModule)
}