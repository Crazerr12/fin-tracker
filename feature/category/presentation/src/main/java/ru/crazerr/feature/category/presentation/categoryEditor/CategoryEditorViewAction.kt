package ru.crazerr.feature.category.presentation.categoryEditor

import ru.crazerr.feature.icon.domain.api.IconModel


sealed interface CategoryEditorViewAction {
    data object BackClick : CategoryEditorViewAction

    data class UpdateName(val name: String) : CategoryEditorViewAction
    data class UpdateColor(val color: Long) : CategoryEditorViewAction
    data class UpdateIcon(val iconModel: IconModel) : CategoryEditorViewAction

    data object SaveClick : CategoryEditorViewAction
}