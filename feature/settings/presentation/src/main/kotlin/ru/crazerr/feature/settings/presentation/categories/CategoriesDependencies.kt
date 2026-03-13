package ru.crazerr.feature.settings.presentation.categories

import ru.crazerr.feature.settings.domain.repository.CategoryRepository

class CategoriesDependencies(
    val args: CategoriesArgs,
    val categoryRepository: CategoryRepository,
)