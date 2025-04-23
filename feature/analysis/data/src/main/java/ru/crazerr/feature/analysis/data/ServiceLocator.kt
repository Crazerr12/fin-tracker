package ru.crazerr.feature.analysis.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.database.databaseModule
import ru.crazerr.feature.analysis.data.dataSource.AnalysisLocalDataSource
import ru.crazerr.feature.analysis.data.repository.AnalysisRepositoryImpl
import ru.crazerr.feature.analysis.domain.repository.AnalysisRepository

val analysisDataModule = module {
    singleOf(::AnalysisRepositoryImpl) { bind<AnalysisRepository>() }
    singleOf(::AnalysisLocalDataSource)

    includes(databaseModule)
}