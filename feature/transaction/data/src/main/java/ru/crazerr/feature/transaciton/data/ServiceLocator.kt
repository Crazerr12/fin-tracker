package ru.crazerr.feature.transaciton.data

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.transaciton.data.repository.TransactionRepositoryImpl

val transactionDataModule = module {
    singleOf(::TransactionRepositoryImpl)
}