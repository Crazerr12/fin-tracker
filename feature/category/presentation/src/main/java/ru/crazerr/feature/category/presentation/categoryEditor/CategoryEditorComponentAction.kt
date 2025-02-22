package ru.crazerr.feature.category.presentation.categoryEditor

import ru.crazerr.feature.domain.api.Category

sealed interface CategoryEditorComponentAction {
    data object BackClick : CategoryEditorComponentAction

    data class SaveClick(val category: Category) : CategoryEditorComponentAction
}