package ru.crazerr.feature.settings.presentation.settings

sealed interface SettingsViewAction {
    data object BackClick : SettingsViewAction
    data object CategoriesClick : SettingsViewAction
}