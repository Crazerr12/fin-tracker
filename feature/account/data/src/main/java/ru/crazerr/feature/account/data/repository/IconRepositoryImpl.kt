package ru.crazerr.feature.account.data.repository

import ru.crazerr.feature.account.data.dataSource.IconLocalDataSource
import ru.crazerr.feature.account.domain.repository.IconRepository
import ru.crazerr.feature.icon.domain.api.IconModel

internal class IconRepositoryImpl(
    private val iconLocalDataSource: IconLocalDataSource,
) : IconRepository {
    override suspend fun getIcons(): Result<List<IconModel>> = iconLocalDataSource.getIcons()
}