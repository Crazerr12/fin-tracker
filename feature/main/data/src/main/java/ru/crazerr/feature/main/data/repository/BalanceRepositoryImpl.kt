package ru.crazerr.feature.main.data.repository

import kotlinx.coroutines.flow.Flow
import ru.crazerr.feature.main.data.dataSource.LocalBalanceDataSource
import ru.crazerr.feature.main.domain.models.IncomeAndExpenses
import ru.crazerr.feature.main.domain.repository.BalanceRepository

internal class BalanceRepositoryImpl(
    private val localBalanceDataSource: LocalBalanceDataSource,
) : BalanceRepository {
    override suspend fun getIncomeAndExpenses(): Flow<Result<IncomeAndExpenses>> {
        return localBalanceDataSource.getIncomeAndExpenses()
    }
}