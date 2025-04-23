package ru.crazerr.feature.category.presentation.categoryEditor

import ru.crazerr.feature.icon.domain.api.IconModel


data class CategoryEditorState(
    val id: Long,
    val name: String,
    val nameError: String,
    val selectedIconModel: IconModel,
    val icons: List<IconModel>,
    val selectedColor: Long,
    val colors: List<Long>,
    val buttonIsLoading: Boolean,
)

internal val InitialCategoryEditorState = CategoryEditorState(
    id = -1,
    name = "",
    nameError = "",
    selectedIconModel = IconModel(id = -1, icon = ByteArray(0), purpose = ""),
    icons = listOf(),
    selectedColor = 0xFFFFD1DC,
    colors = listOf(
        0xFFFFD1DC, // Pastel Pink
        0xFFEFA94A, // Pastel Yellow
        0xFF7FB5B5, // Pastel Turquoise
        0xFF5D9B9B, // Pastel Blue
        0xFFA18594, // Pastel Purple
        0xFF77DD77, // Pastel Green
        0xFFFF7514, // Pastel Orange
        0xFFFF8C69, // Salmon
        0xFFFF9BAA, // Crayola Salmon
        0xFFFFB28B, // Light Yellow-Pink
        0xFFFCE883, // Crayola Yellow
        0xFFBEBD7F, // Green-Beige
        0xFFC6DF90, // Very Light Yellow-Green
        0xFF99FF99, // Salad Green
        0xFFAFDAFC, // Blue Frost
        0xFFE6E6FA, // Lavender
        0xFFFFF0F5, // Pink-Lavender
        0xFFF5F5DC, // Beige
        0xFFE4717A, // Caramel Pink
        0xFFB39F7A, // Cappuccino
        0xFFE6D690, // Light Ivory
        0xFFEAE0C8, // Pearl
        0xFFF2E8C9, // Cream
        0xFFF2DDC6, // Creamy Cream
    ),
    buttonIsLoading = false,
)