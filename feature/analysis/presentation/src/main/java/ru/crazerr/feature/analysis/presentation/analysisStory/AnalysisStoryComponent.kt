package ru.crazerr.feature.analysis.presentation.analysisStory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisComponent
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisComponentAction

class AnalysisStoryComponent(
    componentContext: ComponentContext,
    private val onAction: (AnalysisStoryComponentAction) -> Unit,
    private val dependencies: AnalysisStoryDependencies,
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Analysis,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Config.Analysis -> createAnalysis(componentContext = componentContext)
        }

    private fun createAnalysis(componentContext: ComponentContext): Child.Analysis =
        Child.Analysis(
            component = dependencies.analysisComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        is AnalysisComponentAction.OnCategoryClick -> onAction(
                            AnalysisStoryComponentAction.OnCategoryClick(
                                categoryId = action.categoryId,
                                startDate = action.startDate,
                                endDate = action.endDate,
                                transactionType = action.transactionType,
                            )
                        )
                    }
                })
        )

    sealed interface Child {
        data class Analysis(val component: AnalysisComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Analysis : Config
    }
}