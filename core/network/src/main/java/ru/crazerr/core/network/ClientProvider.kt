package ru.crazerr.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val MAIN_SERVER_URL = ""

internal class ClientProvider() {
    internal fun createHttpClient(): HttpClient =
        HttpClient(CIO) {
            expectSuccess = true

            engine {

            }

            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    useAlternativeNames = true
                })
            }

            defaultRequest {
                url(MAIN_SERVER_URL)
            }
        }
}