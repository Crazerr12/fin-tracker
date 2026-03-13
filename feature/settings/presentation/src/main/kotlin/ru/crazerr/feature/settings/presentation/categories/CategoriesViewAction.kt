package ru.crazerr.feature.settings.presentation.categories

import ru.crazerr.feature.category.presentation.api.model.CategoryUi

sealed interface CategoriesViewAction {
    data object BackClick : CategoriesViewAction
    data object CreateNewCategory : CategoriesViewAction
    data class EditExistCategory(val categoryUi: CategoryUi) : CategoriesViewAction
    data class DeleteCategory(val categoryId: Long) : CategoriesViewAction
}