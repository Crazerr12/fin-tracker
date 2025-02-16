package ru.crazerr.feature.transaciton.data.dataSource

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import ru.crazerr.core.database.transactions.dao.TransactionsDao
import ru.crazerr.core.network.safeRequest
import ru.crazerr.feature.transaciton.data.model.TransactionRequest
import ru.crazerr.feature.transaciton.data.model.TransactionResponse

internal class RemoteTransactionDataSource(
    private val httpClient: HttpClient,
    private val transactionsDao: TransactionsDao
) {
    suspend fun createTransaction(transactionRequest: TransactionRequest): Result<TransactionResponse> {
        return httpClient.safeRequest {  }
    }

    companion object {
        private const val CREATE_TRANSACTION_URL = ""
    }
}