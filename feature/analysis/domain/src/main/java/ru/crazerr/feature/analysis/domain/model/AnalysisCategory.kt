package ru.crazerr.feature.analysis.domain.model

import ru.crazerr.feature.category.domain.api.Category

data class AnalysisCategory(
    val category: Category,
    val moneySpent: Double,
)