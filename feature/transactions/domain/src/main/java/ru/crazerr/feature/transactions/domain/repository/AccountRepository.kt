package ru.crazerr.feature.transactions.domain.repository

import ru.crazerr.feature.account.domain.api.Account

interface AccountRepository {
    suspend fun getAccounts(): Result<List<Account>>
}