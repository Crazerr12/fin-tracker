package ru.crazerr.core.network

import org.koin.dsl.module

val networkModule = module {
    single {
        ClientProvider().createHttpClient()
    }
}