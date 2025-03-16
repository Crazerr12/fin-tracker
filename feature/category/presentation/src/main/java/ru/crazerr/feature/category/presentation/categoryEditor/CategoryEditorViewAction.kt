package ru.crazerr.feature.category.presentation.categoryEditor

import ru.crazerr.feature.domain.api.Icon

sealed interface CategoryEditorViewAction {
    data object BackClick : CategoryEditorViewAction

    data class UpdateName(val name: String) : CategoryEditorViewAction
    data class UpdateColor(val color: Long) : CategoryEditorViewAction
    data class UpdateIcon(val icon: Icon) : CategoryEditorViewAction

    data object SaveClick : CategoryEditorViewAction
}