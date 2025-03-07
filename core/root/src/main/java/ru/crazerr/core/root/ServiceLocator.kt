package ru.crazerr.core.root

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.root.RootComponentImpl.FactoryImpl
import ru.crazerr.core.utils.utilsModule
import ru.crazerr.feature.main.presentation.mainPresentationModule
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponentFactory

val rootModule = module {
    singleOf(::FactoryImpl) { bind<RootComponent.Factory>() }

    includes(utilsModule)
}

internal val storyModules = module {
    includes(mainPresentationModule)
}

internal class DiInjector : KoinComponent {
    val mainStoryComponentFactory: MainStoryComponentFactory by inject()

    companion object {
        fun create(): DiInjector {
            loadKoinModules(storyModules)
            return DiInjector()
        }
    }
}