package ru.crazerr.core.root

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.root.RootComponentImpl.FactoryImpl
import ru.crazerr.core.utils.utilsModule

val rootModule = module {
    singleOf(::FactoryImpl) { bind<RootComponent.Factory>() }

    includes(utilsModule)
}