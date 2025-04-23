package ru.crazerr.feature.analysis.presentation.analysis

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.presentation.ComponentFactory
import ru.crazerr.feature.analysis.domain.repository.AnalysisRepository

interface AnalysisComponentFactory : ComponentFactory<AnalysisComponent, AnalysisComponentAction>

internal class AnalysisComponentFactoryImpl(
    private val analysisRepository: AnalysisRepository,
) : AnalysisComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (AnalysisComponentAction) -> Unit
    ): AnalysisComponent = AnalysisComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = AnalysisDependencies(analysisRepository = analysisRepository),
    )
}