package ru.crazerr.feature.transaction.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.account.presentation.accountPresentationModule
import ru.crazerr.feature.category.presentation.categoryPresentationModule
import ru.crazerr.feature.transaction.data.transactionDataModule
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponentFactory
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponentFactoryImpl
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryComponentFactory
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryComponentFactoryImpl

val transactionPresentationModule = module {
    singleOf(::TransactionEditorComponentFactoryImpl) { bind<TransactionEditorComponentFactory>() }
    singleOf(::TransactionEditorStoryComponentFactoryImpl) { bind<TransactionEditorStoryComponentFactory>() }

    includes(transactionDataModule, accountPresentationModule, categoryPresentationModule)
}