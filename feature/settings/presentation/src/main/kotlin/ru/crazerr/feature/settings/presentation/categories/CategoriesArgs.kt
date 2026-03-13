package ru.crazerr.feature.settings.presentation.categories

import kotlinx.coroutines.flow.SharedFlow

class CategoriesArgs(
    val input: SharedFlow<CategoriesComponent.Input>,
)