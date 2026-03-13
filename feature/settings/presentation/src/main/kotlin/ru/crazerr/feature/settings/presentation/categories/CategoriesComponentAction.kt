package ru.crazerr.feature.settings.presentation.categories

import ru.crazerr.feature.category.presentation.api.model.CategoryUi

sealed interface CategoriesComponentAction {
    data object BackClick : CategoriesComponentAction
    data class EditExistCategory(val categoryUi: CategoryUi) : CategoriesComponentAction
    data object CreateNewCategory : CategoriesComponentAction
}