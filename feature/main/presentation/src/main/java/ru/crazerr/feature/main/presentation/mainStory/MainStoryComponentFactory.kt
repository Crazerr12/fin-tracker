package ru.crazerr.feature.main.presentation.mainStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.account.presentation.AccountEditorComponentFactory
import ru.crazerr.feature.main.presentation.main.MainComponentFactory
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponentFactory

interface MainStoryComponentFactory {
    fun create(componentContext: ComponentContext): MainStoryComponent
}

internal class MainStoryComponentFactoryImpl(
    private val mainComponentFactory: MainComponentFactory,
    private val accountEditorComponentFactory: AccountEditorComponentFactory,
    private val settingsStoryComponentFactory: SettingsStoryComponentFactory,
) : MainStoryComponentFactory {
    override fun create(componentContext: ComponentContext): MainStoryComponent =
        MainStoryComponent(
            componentContext = componentContext,
            dependencies = MainStoryDependencies(
                mainComponentFactory = mainComponentFactory,
                accountEditorComponentFactory = accountEditorComponentFactory,
                settingsStoryComponentFactory = settingsStoryComponentFactory,
            )
        )
}