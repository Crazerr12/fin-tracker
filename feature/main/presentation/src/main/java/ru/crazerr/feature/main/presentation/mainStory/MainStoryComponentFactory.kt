package ru.crazerr.feature.main.presentation.mainStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.feature.account.presentation.AccountEditorComponentFactory
import ru.crazerr.feature.main.presentation.main.MainComponentFactory

interface MainStoryComponentFactory {
    fun create(componentContext: ComponentContext): MainStoryComponent
}

internal class MainStoryComponentFactoryImpl(
    private val mainComponentFactory: MainComponentFactory,
    private val accountEditorComponentFactory: AccountEditorComponentFactory
) : MainStoryComponentFactory {
    override fun create(componentContext: ComponentContext): MainStoryComponent =
        MainStoryComponent(
            componentContext = componentContext,
            dependencies = MainStoryDependencies(
                mainComponentFactory = mainComponentFactory,
                accountEditorComponentFactory = accountEditorComponentFactory
            )
        )
}