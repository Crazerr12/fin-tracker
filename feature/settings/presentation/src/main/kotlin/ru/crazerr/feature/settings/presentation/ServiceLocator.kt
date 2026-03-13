package ru.crazerr.feature.settings.presentation

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.feature.category.presentation.categoryPresentationModule
import ru.crazerr.feature.settings.data.settingsDataModule
import ru.crazerr.feature.settings.presentation.categories.CategoriesComponentFactory
import ru.crazerr.feature.settings.presentation.categories.CategoriesComponentFactoryImpl
import ru.crazerr.feature.settings.presentation.settings.SettingsComponentFactory
import ru.crazerr.feature.settings.presentation.settings.SettingsComponentFactoryImpl
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponentFactory
import ru.crazerr.feature.settings.presentation.settingsStory.SettingsStoryComponentFactoryImpl

val settingsPresentationModule = module {
    singleOf(::SettingsComponentFactoryImpl) { bind<SettingsComponentFactory>() }

    singleOf(::CategoriesComponentFactoryImpl) { bind<CategoriesComponentFactory>() }

    singleOf(::SettingsStoryComponentFactoryImpl) { bind<SettingsStoryComponentFactory>() }

    includes(settingsDataModule, categoryPresentationModule)
}