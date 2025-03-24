package ru.crazerr.feature.budget.domain

import java.time.LocalDate

interface TransactionRepository {
    suspend fun getCurrentAmount(categoryId: Long, date: LocalDate): Result<Double>
}