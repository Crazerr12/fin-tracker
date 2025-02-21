package ru.crazerr.feature.account.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.account.data.accountDataModule

val accountPresentationModule = module {
    singleOf(::AccountEditorComponentFactoryImpl) { bind<AccountEditorComponentFactory>() }

    includes(accountDataModule)
}