package ru.crazerr.feature.settings.presentation.settings

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.presentation.ComponentFactory

interface SettingsComponentFactory : ComponentFactory<SettingsComponent, SettingsComponentAction>

internal class SettingsComponentFactoryImpl() : SettingsComponentFactory {
    override fun create(
        componentContext: ComponentContext,
        onAction: (SettingsComponentAction) -> Unit
    ): SettingsComponent = SettingsComponent(
        componentContext = componentContext,
        onAction = onAction,
        dependencies = SettingsDependencies(),
    )
}