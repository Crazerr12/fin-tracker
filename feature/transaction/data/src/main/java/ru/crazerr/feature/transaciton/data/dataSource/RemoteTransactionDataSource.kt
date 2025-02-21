package ru.crazerr.feature.transaciton.data.dataSource

import io.ktor.client.HttpClient
import ru.crazerr.core.network.safeRequest
import ru.crazerr.feature.transaction.domain.api.Transaction

internal class RemoteTransactionDataSource(
    private val httpClient: HttpClient,
) {
    suspend fun createTransaction(transaction: Transaction): Result<Transaction> {
        return httpClient.safeRequest {  }
    }

    companion object {
        private const val CREATE_TRANSACTION_URL = ""
    }
}