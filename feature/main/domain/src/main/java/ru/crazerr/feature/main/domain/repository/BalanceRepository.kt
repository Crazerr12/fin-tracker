package ru.crazerr.feature.main.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.main.domain.models.IncomeAndExpenses

interface BalanceRepository {
    suspend fun getIncomeAndExpenses(): Flow<Result<IncomeAndExpenses>>
}