package ru.crazerr.feature.analysis.presentation.analysisStory

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.presentation.ComponentFactory
import ru.crazerr.feature.analysis.presentation.analysis.AnalysisComponentFactory

interface AnalysisStoryComponentFactory :
    ComponentFactory<AnalysisStoryComponent, AnalysisStoryComponentAction>

internal class AnalysisStoryComponentFactoryImpl(
    private val analysisComponentFactory: AnalysisComponentFactory,
) : AnalysisStoryComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (AnalysisStoryComponentAction) -> Unit
    ): AnalysisStoryComponent = AnalysisStoryComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = AnalysisStoryDependencies(analysisComponentFactory = analysisComponentFactory)
    )
}