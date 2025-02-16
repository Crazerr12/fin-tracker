package ru.crazerr.feature.transaciton.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TransactionRequest(
    val id: Long,
    @SerialName("category_id") val categoryId: Long,
    val amount: Long,
    val type: Boolean,
    val date: String,
    @SerialName("account_id") val accountId: Long,
)