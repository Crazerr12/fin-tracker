package ru.crazerr.feature.transactions.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.transaction.presentation.transactionPresentationModule
import ru.crazerr.feature.transactions.data.transactionsDataModule
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponentFactory
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponentFactoryImpl
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponentFactory
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponentFactoryImpl
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryComponentFactory
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryComponentFactoryImpl

val transactionsPresentationModule = module {
    singleOf(::TransactionsComponentFactoryImpl) { bind<TransactionsComponentFactory>() }
    singleOf(::TransactionsFilterComponentFactoryImpl) { bind<TransactionsFilterComponentFactory>() }
    singleOf(::TransactionsStoryComponentFactoryImpl) { bind<TransactionsStoryComponentFactory>() }

    includes(transactionsDataModule, transactionPresentationModule)
}