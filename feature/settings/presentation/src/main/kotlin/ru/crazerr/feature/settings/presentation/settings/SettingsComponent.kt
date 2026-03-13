package ru.crazerr.feature.settings.presentation.settings

import com.arkivanov.decompose.ComponentContext
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope

class SettingsComponent(
    componentContext: ComponentContext,
    private val onAction: (SettingsComponentAction) -> Unit,
    private val dependencies: SettingsDependencies,
) : BaseComponent<SettingsState, SettingsViewAction>(SettingsState()),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()

    override fun handleViewAction(action: SettingsViewAction) {
        when (action) {
            SettingsViewAction.CategoriesClick -> onAction(SettingsComponentAction.CategoriesClick)
            SettingsViewAction.BackClick -> onAction(SettingsComponentAction.BackClick)
        }
    }
}