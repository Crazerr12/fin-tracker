package ru.crazerr.feature.settings.presentation.settingsStory

sealed interface SettingsStoryComponentAction {
    data object BackClick : SettingsStoryComponentAction
}