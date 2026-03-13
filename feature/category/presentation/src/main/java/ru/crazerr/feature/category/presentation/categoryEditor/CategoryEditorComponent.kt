package ru.crazerr.feature.category.presentation.categoryEditor

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.category.presentation.R
import ru.crazerr.feature.category.presentation.api.mapper.toCategory
import ru.crazerr.feature.category.presentation.api.model.CategoryUi
import ru.crazerr.feature.icon.presentation.api.mapper.toIconUi
import ru.crazerr.feature.icon.presentation.api.model.IconUi

class CategoryEditorComponent(
    componentContext: ComponentContext,
    private val onAction: (CategoryEditorComponentAction) -> Unit,
    private val dependencies: CategoryEditorDependencies,
) : BaseComponent<CategoryEditorState, CategoryEditorViewAction>(
    CategoryEditorState(
        id = dependencies.args.categoryUi?.id ?: -1L,
        name = dependencies.args.categoryUi?.name ?: "",
        selectedIcon = dependencies.args.categoryUi?.iconUi ?: IconUi(-1, ByteArray(0), ""),
        selectedColor = Color(dependencies.args.categoryUi?.color ?: 1).value.toLong()
    )
), ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        getInitData()
    }

    override fun handleViewAction(action: CategoryEditorViewAction) {
        when (action) {
            CategoryEditorViewAction.BackClick -> onAction(CategoryEditorComponentAction.BackClick)
            CategoryEditorViewAction.SaveClick -> onSaveClick()
            is CategoryEditorViewAction.UpdateColor -> onUpdateColor(color = action.color)
            is CategoryEditorViewAction.UpdateIcon -> onUpdateIcon(icon = action.icon)
            is CategoryEditorViewAction.UpdateName -> onUpdateName(name = action.name)
        }
    }

    private fun getInitData() {
        coroutineScope.launch {
            val iconsDeferred = async {
                dependencies.iconRepository.getIcons().map { it.map { it.toIconUi() } }
            }

            iconsDeferred.await().fold(
                onSuccess = {
                    reduceState {
                        copy(
                            selectedIcon = dependencies.args.categoryUi?.iconUi ?: icons[0],
                            icons = it
                        )
                    }
                },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )

            reduceState { copy(buttonIsLoading = false) }
        }
    }

    private fun onSaveClick() {
        validateUserInput {
            reduceState { copy(buttonIsLoading = true) }
            coroutineScope.launch {
                val result = if (dependencies.args.categoryUi != null) {
                    dependencies.categoryRepository.updateCategory(
                        category = CategoryUi(
                            id = state.value.id,
                            name = state.value.name,
                            color = state.value.selectedColor,
                            iconUi = state.value.selectedIcon,
                            isTemplate = false,
                        ).toCategory()
                    )
                } else {
                    dependencies.categoryRepository.createCategory(
                        category = CategoryUi(
                            id = 0,
                            name = state.value.name,
                            color = state.value.selectedColor,
                            iconUi = state.value.selectedIcon,
                            isTemplate = false,
                        ).toCategory()
                    )
                }

                result.fold(
                    onSuccess = {
                        reduceState { copy(buttonIsLoading = false) }
                        onAction(CategoryEditorComponentAction.SaveClick(it))
                    },
                    onFailure = {
                        snackbarManager.showSnackbar(it.localizedMessage ?: "")
                        reduceState { copy(buttonIsLoading = false) }
                    }
                )
            }
        }
    }

    private fun onUpdateColor(color: Long) {
        reduceState { copy(selectedColor = color) }
    }

    private fun onUpdateName(name: String) {
        reduceState { copy(name = name, nameError = "") }
    }

    private fun onUpdateIcon(icon: IconUi) {
        reduceState { copy(selectedIcon = icon) }
    }

    private fun validateUserInput(block: () -> Unit) {
        var isValid = true

        if (state.value.name.isEmpty()) {
            reduceState { copy(nameError = dependencies.resourceManager.getString(R.string.category_editor_name_empty_error)) }
            isValid = false
        } else if (state.value.name.length < 3) {
            reduceState { copy(nameError = dependencies.resourceManager.getString(R.string.category_editor_name_minimal_range_error)) }
            isValid = false
        }

        if (isValid) {
            block()
        }
    }
}