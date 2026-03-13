package ru.crazerr.feature.settings.presentation.settingsStory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import ru.crazerr.feature.category.presentation.api.mapper.toCategoryUi
import ru.crazerr.feature.category.presentation.api.model.CategoryUi
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorArgs
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponent
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentAction
import ru.crazerr.feature.settings.presentation.categories.CategoriesArgs
import ru.crazerr.feature.settings.presentation.categories.CategoriesComponent
import ru.crazerr.feature.settings.presentation.categories.CategoriesComponentAction
import ru.crazerr.feature.settings.presentation.settings.SettingsComponent
import ru.crazerr.feature.settings.presentation.settings.SettingsComponentAction

class SettingsStoryComponent(
    componentContext: ComponentContext,
    private val onAction: (SettingsStoryComponentAction) -> Unit,
    private val dependencies: SettingsStoryDependencies,
) : ComponentContext by componentContext {
    private val input: MutableSharedFlow<CategoriesComponent.Input> =
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Settings,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun onBackClick() {
        navigation.pop()
    }

    private fun child(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            Config.Categories -> createCategories(componentContext = componentContext)
            Config.Settings -> createSettings(componentContext = componentContext)
            is Config.CategoryEditor -> createCategoryEditor(
                componentContext = componentContext,
                config = config,
            )
        }
    }

    private fun createCategories(componentContext: ComponentContext): Child.Categories =
        Child.Categories(
            component = dependencies.categoriesComponentFactory.create(
                componentContext = componentContext,
                args = CategoriesArgs(input = input),
                onAction = { action ->
                    when (action) {
                        CategoriesComponentAction.BackClick -> onBackClick()
                        CategoriesComponentAction.CreateNewCategory -> navigation.push(
                            Config.CategoryEditor(null)
                        )

                        is CategoriesComponentAction.EditExistCategory -> navigation.push(
                            Config.CategoryEditor(
                                categoryUi = action.categoryUi,
                            )
                        )
                    }
                })
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createSettings(componentContext: ComponentContext): Child.Settings =
        Child.Settings(
            component = dependencies.settingsComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        SettingsComponentAction.CategoriesClick -> navigation.push(Config.Categories)
                        SettingsComponentAction.BackClick -> onAction(SettingsStoryComponentAction.BackClick)
                    }
                })
        )

    private fun createCategoryEditor(
        componentContext: ComponentContext,
        config: Config.CategoryEditor
    ): Child.CategoryEditor =
        Child.CategoryEditor(
            component = dependencies.categoryEditorComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        CategoryEditorComponentAction.BackClick -> onBackClick()
                        is CategoryEditorComponentAction.SaveClick -> {
                            input.tryEmit(CategoriesComponent.Input.CategoryInput(categoryUi = action.category.toCategoryUi()))
                            onBackClick()
                        }
                    }
                },
                args = CategoryEditorArgs(categoryUi = config.categoryUi),
            )
        )

    sealed interface Child {
        data class Settings(val component: SettingsComponent) : Child
        data class Categories(val component: CategoriesComponent) : Child
        data class CategoryEditor(val component: CategoryEditorComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Settings : Config

        @Serializable
        data object Categories : Config

        @Serializable
        data class CategoryEditor(val categoryUi: CategoryUi?) : Config
    }
}