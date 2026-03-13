package ru.crazerr.feature.icon.presentation.api.mapper

import ru.crazerr.feature.icon.domain.api.IconModel
import ru.crazerr.feature.icon.presentation.api.model.IconUi

fun IconModel.toIconUi() = IconUi(id = id, icon = icon, purpose = purpose)

fun IconUi.toIconModel() = IconModel(id = id, icon = icon, purpose = purpose)