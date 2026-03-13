package ru.crazerr.feature.settings.presentation.settings

sealed interface SettingsComponentAction {
    data object BackClick : SettingsComponentAction
    data object CategoriesClick : SettingsComponentAction
}