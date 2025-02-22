package ru.crazerr.feature.category.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.category.data.categoryDataModule
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactory
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentFactoryImpl

val presentationCategoryModule = module {
    singleOf(::CategoryEditorComponentFactoryImpl) { bind<CategoryEditorComponentFactory>() }

    includes(categoryDataModule)
}