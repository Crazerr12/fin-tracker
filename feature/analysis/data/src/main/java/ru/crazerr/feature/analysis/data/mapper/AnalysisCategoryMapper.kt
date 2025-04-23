package ru.crazerr.feature.analysis.data.mapper

import ru.crazerr.core.database.categories.model.CategoryWithIconAndSum
import ru.crazerr.feature.analysis.domain.model.AnalysisCategory
import ru.crazerr.feature.category.data.api.toCategory

fun CategoryWithIconAndSum.toAnalysisCategory() = AnalysisCategory(
    category = categoryWithIcon.toCategory(),
    moneySpent = totalSum,
)