package ru.crazerr.feature.category.presentation.categoryEditor

import ru.crazerr.feature.icon.presentation.api.model.IconUi


data class CategoryEditorState(
    val id: Long = -1,
    val name: String = "",
    val nameError: String = "",
    val selectedIcon: IconUi = IconUi(id = -1, icon = ByteArray(0), purpose = ""),
    val icons: List<IconUi> = emptyList(),
    val selectedColor: Long = 0xFFE91E63,
    val colors: List<Long> = initialColors,
    val buttonIsLoading: Boolean = true,
)

private val initialColors = listOf(
    0xFFE91E63, // Pink
    0xFF2196F3, // Blue
    0xFF4CAF50, // Green
    0xFFFF5722, // Deep Orange
    0xFF9C27B0, // Purple
    0xFFFFC107, // Amber
    0xFF00BCD4, // Cyan
    0xFF795548, // Brown
    0xFF3F51B5, // Indigo
    0xFFFF9800, // Orange
    0xFF009688, // Teal
    0xFF673AB7, // Deep Purple
    0xFF607D8B, // Blue Grey
    0xFF8BC34A, // Light Green
    0xFFFFEB3B, // Yellow
    0xFFCDDC39, // Lime
    0xFFF44336, // Red
    0xFF00ACC1, // Dark Cyan
    0xFF6D4C41, // Dark Brown
    0xFF1E88E5, // Dark Blue
    0xFF5E35B1, // Dark Purple
    0xFF43A047, // Dark Green
    0xFFD81B60, // Dark Pink
    0xFF7E57C2  // Muted Violet
)