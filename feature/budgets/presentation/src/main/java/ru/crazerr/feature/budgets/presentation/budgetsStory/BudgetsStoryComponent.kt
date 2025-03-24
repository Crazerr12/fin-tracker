package ru.crazerr.feature.budgets.presentation.budgetsStory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryArgs
import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryComponent
import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryComponentAction
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponent
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponentAction

class BudgetsStoryComponent(
    componentContext: ComponentContext,
    private val dependencies: BudgetsStoryDependencies,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Budgets,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.BudgetStory -> createBudgetStory(
                config = config,
                componentContext = componentContext
            )

            Config.Budgets -> createBudgets(componentContext = componentContext)
        }

    private fun createBudgetStory(
        componentContext: ComponentContext,
        config: Config.BudgetStory
    ): Child.BudgetStory = Child.BudgetStory(
        component = dependencies.budgetStoryComponentFactory.create(
            componentContext = componentContext,
            onAction = { action ->
                when (action) {
                    BudgetStoryComponentAction.BackClick -> onBackClick()
                    is BudgetStoryComponentAction.SaveClick -> onBackClick()
                }
            },
            args = BudgetStoryArgs(budgetId = config.budgetId)
        )
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun createBudgets(componentContext: ComponentContext): Child.Budgets = Child.Budgets(
        component = dependencies.budgetsComponentFactory.create(
            componentContext = componentContext,
            onAction = { action ->
                when (action) {
                    is BudgetsComponentAction.GoToBudgetEditor -> navigation.push(
                        Config.BudgetStory(
                            budgetId = action.budgetId
                        )
                    )
                }
            })
    )

    private fun onBackClick() = navigation.pop()

    sealed interface Child {
        data class Budgets(val component: BudgetsComponent) : Child
        data class BudgetStory(val component: BudgetStoryComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Budgets : Config

        @Serializable
        data class BudgetStory(val budgetId: Long) : Config
    }
}