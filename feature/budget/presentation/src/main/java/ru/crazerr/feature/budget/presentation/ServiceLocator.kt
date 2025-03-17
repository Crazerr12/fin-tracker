package ru.crazerr.feature.budget.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.budget.data.budgetDataModule
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorComponentFactory
import ru.crazerr.feature.budget.presentation.budgetEditor.BudgetEditorComponentFactoryImpl
import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryComponentFactory
import ru.crazerr.feature.budget.presentation.budgetStory.BudgetStoryComponentFactoryImpl
import ru.crazerr.feature.category.presentation.categoryPresentationModule

val budgetPresentationModule = module {
    singleOf(::BudgetEditorComponentFactoryImpl) { bind<BudgetEditorComponentFactory>() }
    singleOf(::BudgetStoryComponentFactoryImpl) { bind<BudgetStoryComponentFactory>() }

    includes(budgetDataModule, categoryPresentationModule)
}