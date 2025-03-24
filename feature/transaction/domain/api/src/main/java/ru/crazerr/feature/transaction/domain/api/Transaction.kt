package ru.crazerr.feature.transaction.domain.api

import ru.crazerr.feature.account.domain.api.Account
import ru.crazerr.feature.domain.api.Category
import java.time.LocalDate

data class Transaction(
    val id: Long,
    val category: Category,
    val amount: Double,
    val type: TransactionType,
    val date: LocalDate,
    val account: Account,
)