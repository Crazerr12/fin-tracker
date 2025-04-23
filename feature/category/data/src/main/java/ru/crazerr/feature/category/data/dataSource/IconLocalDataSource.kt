package ru.crazerr.feature.category.data.dataSource

import ru.crazerr.core.database.AppDatabase
import ru.crazerr.feature.icon.data.api.toIcon
import ru.crazerr.feature.icon.domain.api.IconModel

internal class IconLocalDataSource(
    private val appDatabase: AppDatabase,
) {
    suspend fun getIcons(): Result<List<IconModel>> = runCatching {
        appDatabase.iconsDao().getIconsByPurpose(purpose = "category").map { it.toIcon() }
    }
}