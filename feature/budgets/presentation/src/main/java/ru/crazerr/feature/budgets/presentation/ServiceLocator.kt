package ru.crazerr.feature.budgets.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.budget.presentation.budgetPresentationModule
import ru.crazerr.feature.budgets.data.budgetsDataModule
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponentFactory
import ru.crazerr.feature.budgets.presentation.budgets.BudgetsComponentFactoryImpl
import ru.crazerr.feature.budgets.presentation.budgetsStory.BudgetsStoryComponentFactory
import ru.crazerr.feature.budgets.presentation.budgetsStory.BudgetsStoryComponentFactoryImpl

val budgetsPresentationModule = module {
    singleOf(::BudgetsComponentFactoryImpl) { bind<BudgetsComponentFactory>() }
    singleOf(::BudgetsStoryComponentFactoryImpl) { bind<BudgetsStoryComponentFactory>() }

    includes(budgetsDataModule, budgetPresentationModule)
}