package ru.crazerr.feature.analysis.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.analysis.data.analysisDataModule
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisComponentFactory
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisComponentFactoryImpl
import ru.crazerr.feature.analysis.presentation.analysisStory.AnalysisStoryComponentFactory
import ru.crazerr.feature.analysis.presentation.analysisStory.AnalysisStoryComponentFactoryImpl

val analysisPresentationModule = module {
    singleOf(::AnalysisComponentFactoryImpl) { bind<AnalysisComponentFactory>() }
    singleOf(::AnalysisStoryComponentFactoryImpl) { bind<AnalysisStoryComponentFactory>() }

    includes(analysisDataModule)
}