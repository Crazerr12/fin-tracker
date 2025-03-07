package ru.crazerr.feature.main.presentation.mainStory

import ru.crazerr.feature.account.presentation.AccountEditorComponentFactory
import ru.crazerr.feature.main.presentation.main.MainComponentFactory

class MainStoryDependencies(
    val mainComponentFactory: MainComponentFactory,
    val accountEditorComponentFactory: AccountEditorComponentFactory,
)