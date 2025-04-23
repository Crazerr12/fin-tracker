package ru.crazerr.feature.category.data.repository

import ru.crazerr.feature.category.data.dataSource.IconLocalDataSource
import ru.crazerr.feature.category.domain.repository.IconRepository
import ru.crazerr.feature.icon.domain.api.IconModel

internal class IconRepositoryImpl(
    private val iconLocalDataSource: IconLocalDataSource,
) : IconRepository {
    override suspend fun getIcons(): Result<List<IconModel>> = iconLocalDataSource.getIcons()
}