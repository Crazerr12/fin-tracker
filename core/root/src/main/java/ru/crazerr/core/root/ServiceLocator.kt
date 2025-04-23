package ru.crazerr.core.root

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.root.RootComponentImpl.FactoryImpl
import ru.crazerr.core.utils.utilsModule
import ru.crazerr.feature.analysis.presentation.analysisPresentationModule
import ru.crazerr.feature.analysis.presentation.analysisStory.AnalysisStoryComponentFactory
import ru.crazerr.feature.budgets.presentation.budgetsPresentationModule
import ru.crazerr.feature.budgets.presentation.budgetsStory.BudgetsStoryComponentFactory
import ru.crazerr.feature.main.presentation.mainPresentationModule
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponentFactory
import ru.crazerr.feature.transactions.presentation.transactionsPresentationModule
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryComponentFactory

val rootModule = module {
    singleOf(::FactoryImpl) { bind<RootComponent.Factory>() }

    includes(utilsModule)
}

internal val storyModules = module {
    includes(
        mainPresentationModule,
        transactionsPresentationModule,
        budgetsPresentationModule,
        analysisPresentationModule,
    )
}

internal class DiInjector : KoinComponent {
    val mainStoryComponentFactory: MainStoryComponentFactory by inject()
    val transactionsStoryComponentFactory: TransactionsStoryComponentFactory by inject()
    val budgetsStoryComponentFactory: BudgetsStoryComponentFactory by inject()
    val analysisStoryComponentFactory: AnalysisStoryComponentFactory by inject()

    companion object {
        fun create(): DiInjector {
            loadKoinModules(storyModules)
            return DiInjector()
        }
    }
}