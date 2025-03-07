package ru.crazerr.feature.main.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.account.presentation.accountPresentationModule
import ru.crazerr.feature.main.data.mainDataModule
import ru.crazerr.feature.main.presentation.main.MainComponentFactory
import ru.crazerr.feature.main.presentation.main.MainComponentFactoryImpl
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponentFactory
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponentFactoryImpl

val mainPresentationModule = module {
    singleOf(::MainComponentFactoryImpl) { bind<MainComponentFactory>() }
    singleOf(::MainStoryComponentFactoryImpl) { bind<MainStoryComponentFactory>() }

    includes(mainDataModule, accountPresentationModule)
}