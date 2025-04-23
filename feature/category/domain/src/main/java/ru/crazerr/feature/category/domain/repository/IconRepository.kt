package ru.crazerr.feature.category.domain.repository

import ru.crazerr.feature.icon.domain.api.IconModel

interface IconRepository {
    suspend fun getIcons(): Result<List<IconModel>>
}