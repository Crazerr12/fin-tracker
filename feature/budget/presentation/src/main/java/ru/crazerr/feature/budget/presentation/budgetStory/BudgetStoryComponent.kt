package ru.crazerr.feature.budget.presentation.budgetStory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorArgs
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorComponent
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorComponentAction
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorArgs
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponent
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentAction

class BudgetStoryComponent(
    componentContext: ComponentContext,
    private val onAction: (BudgetStoryComponentAction) -> Unit,
    private val dependencies: BudgetStoryDependencies,
) : ComponentContext by componentContext {
    private val input =
        MutableSharedFlow<BudgetEditorComponent.Input>(extraBufferCapacity = Int.MAX_VALUE)

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.BudgetEditor(),
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::childFactory
    )

    private fun childFactory(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.BudgetEditor -> createBudgetEditor(componentContext = componentContext)
            Config.CategoryEditor -> createCategoryEditor(componentContext = componentContext)
        }

    private fun createBudgetEditor(componentContext: ComponentContext): Child.BudgetEditor =
        Child.BudgetEditor(
            component = dependencies.budgetEditorComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        BudgetEditorComponentAction.BackClick -> onAction(BudgetStoryComponentAction.BackClick)
                        is BudgetEditorComponentAction.SaveClick -> onAction(
                            BudgetStoryComponentAction.SaveClick(action.budget)
                        )
                    }
                },
                args = BudgetEditorArgs(inputFlow = input, id = dependencies.args.budgetId)
            )
        )

    private fun createCategoryEditor(componentContext: ComponentContext): Child.CategoryEditor =
        Child.CategoryEditor(
            component = dependencies.categoryEditorComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        CategoryEditorComponentAction.BackClick -> onBackClick()
                        is CategoryEditorComponentAction.SaveClick -> {
                            input.tryEmit(BudgetEditorComponent.Input.AddCategory(category = action.category))
                            onBackClick()
                        }
                    }
                },
                args = CategoryEditorArgs()
            )
        )

    private fun onBackClick() = navigation.pop()

    sealed interface Child {
        data class BudgetEditor(val component: BudgetEditorComponent) : Child
        data class CategoryEditor(val component: CategoryEditorComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data class BudgetEditor(val id: Int = -1) : Config

        @Serializable
        data object CategoryEditor : Config
    }
}