package ru.crazerr.feature.transaction.domain

import ru.crazerr.feature.account.domain.api.Account

interface AccountRepository {
    suspend fun getAccounts(): Result<List<Account>>
}