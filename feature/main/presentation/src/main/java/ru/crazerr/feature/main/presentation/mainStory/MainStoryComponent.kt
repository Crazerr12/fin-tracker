package ru.crazerr.feature.main.presentation.mainStory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.crazerr.feature.account.presentation.AccountEditorArgs
import ru.crazerr.feature.account.presentation.AccountEditorComponent
import ru.crazerr.feature.account.presentation.AccountEditorComponentAction
import ru.crazerr.feature.main.presentation.main.MainComponent
import ru.crazerr.feature.main.presentation.main.MainComponentAction
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponent.Config.*
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponent
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponentAction

class MainStoryComponent(
    componentContext: ComponentContext,
    private val dependencies: MainStoryDependencies,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Main,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): Child =
        when (config) {
            is AccountEditor -> createAccountEditorComponent(
                componentContext = componentContext,
                config = config
            )

            Main -> createMainComponent(componentContext = componentContext)
            SettingsStory -> createSettingsStoryComponent(componentContext = componentContext)
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun createMainComponent(componentContext: ComponentContext): Child.Main =
        Child.Main(
            component = dependencies.mainComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        is MainComponentAction.GoToAccount -> navigation.push(
                            AccountEditor(
                                id = action.id
                            )
                        )

                        MainComponentAction.GoToSettings -> navigation.push(SettingsStory)
                    }
                },
            )
        )

    private fun createAccountEditorComponent(
        componentContext: ComponentContext,
        config: Config.AccountEditor
    ): Child.AccountEditor = Child.AccountEditor(
        component = dependencies.accountEditorComponentFactory.create(
            componentContext = componentContext,
            onAction = { action ->
                when (action) {
                    AccountEditorComponentAction.BackClick -> onBackClick()
                    is AccountEditorComponentAction.SaveAccount -> onBackClick()
                }
            },
            args = AccountEditorArgs(accountId = config.id)
        )
    )

    private fun createSettingsStoryComponent(componentContext: ComponentContext): Child.SettingsStory =
        Child.SettingsStory(
            component = dependencies.settingsStoryComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        SettingsStoryComponentAction.BackClick -> onBackClick()
                    }
                })
        )

    private fun onBackClick() {
        navigation.pop()
    }

    sealed interface Child {
        class Main(val component: MainComponent) : Child
        class AccountEditor(val component: AccountEditorComponent) : Child
        class SettingsStory(val component: SettingsStoryComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data class AccountEditor(val id: Long) : Config

        @Serializable
        data object SettingsStory : Config
    }
}