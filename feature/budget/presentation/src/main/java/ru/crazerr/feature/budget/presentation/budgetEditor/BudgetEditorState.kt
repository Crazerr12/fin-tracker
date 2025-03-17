package ru.crazerr.feature.budget.presentation.budgetEditor

import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.domain.api.IconModel
import java.time.LocalDate

data class BudgetEditorState(
    val id: Int,
    val repeatBudgetId: Int?,
    val categories: List<Category>,
    val selectedCategory: Category,
    val categoriesDropdownIsExpand: Boolean,
    val currentAmount: Long,
    val maxAmount: String,
    val maxAmountError: String,
    val isRegular: Boolean,
    val isAlarm: Boolean,
    val isWarning: Boolean,
    val date: LocalDate,
    val buttonIsLoading: Boolean,
)

internal val InitialBudgetEditorState = BudgetEditorState(
    id = -1,
    repeatBudgetId = null,
    categories = emptyList(),
    selectedCategory = Category(
        id = -1,
        name = "",
        color = 0,
        iconModel = IconModel(id = -1, icon = ByteArray(0)),
        isTemplate = true,
    ),
    categoriesDropdownIsExpand = false,
    currentAmount = 0,
    maxAmount = "",
    maxAmountError = "",
    isRegular = false,
    isAlarm = false,
    isWarning = true,
    date = LocalDate.now(),
    buttonIsLoading = false,
)
