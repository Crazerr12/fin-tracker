package ru.crazerr.feature.category.presentation.categoryEditor

sealed interface CategoryEditorViewAction {
    data object BackClick : CategoryEditorViewAction

    data class UpdateName(val name: String) : CategoryEditorViewAction
    data class UpdateColor(val color: Long) : CategoryEditorViewAction
    data class UpdateIcon(val icon: String) : CategoryEditorViewAction

    data object SaveClick : CategoryEditorViewAction
}