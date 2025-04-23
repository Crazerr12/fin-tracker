package ru.crazerr.feature.account.domain.repository

import ru.crazerr.feature.icon.domain.api.IconModel

interface IconRepository {
    suspend fun getIcons(): Result<List<IconModel>>
}