package ru.crazerr.core.network

val networkModule = module {
    single {
        ClientProvider().createHttpClient()
    }
}