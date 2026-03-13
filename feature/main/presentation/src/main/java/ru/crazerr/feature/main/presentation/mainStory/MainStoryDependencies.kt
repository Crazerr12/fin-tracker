package ru.crazerr.feature.main.presentation.mainStory

import ru.crazerr.feature.account.presentation.AccountEditorComponentFactory
import ru.crazerr.feature.main.presentation.main.MainComponentFactory
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponentFactory

class MainStoryDependencies(
    val mainComponentFactory: MainComponentFactory,
    val accountEditorComponentFactory: AccountEditorComponentFactory,
    val settingsStoryComponentFactory: SettingsStoryComponentFactory,
)