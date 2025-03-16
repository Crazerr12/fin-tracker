package ru.crazerr.feature.category.data.dataSource

import ru.crazerr.core.database.categories.dao.CategoriesDao
import ru.crazerr.feature.category.data.api.toCategory
import ru.crazerr.feature.category.data.api.toCategoryEntity
import ru.crazerr.feature.domain.api.Category

internal class CategoryLocalDataSource(
    private val categoriesDao: CategoriesDao,
) {
    suspend fun createCategory(category: Category): Result<Category> = try {
        val id = categoriesDao.insert(category.toCategoryEntity())[0]

        Result.success(category.copy(id = id.toInt()))
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    suspend fun getCategoryById(id: Int): Result<Category> = try {
        val categoryEntity = categoriesDao.getCategoryById(id = id)

        Result.success(categoryEntity.toCategory())
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    suspend fun updateCategory(category: Category): Result<Category> = try {
        categoriesDao.update(category.toCategoryEntity())

        Result.success(category)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}