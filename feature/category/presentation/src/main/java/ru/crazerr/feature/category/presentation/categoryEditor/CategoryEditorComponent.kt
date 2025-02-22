package ru.crazerr.feature.category.presentation.categoryEditor

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.category.presentation.R
import ru.crazerr.feature.domain.api.Category

class CategoryEditorComponent(
    componentContext: ComponentContext,
    private val onAction: (CategoryEditorComponentAction) -> Unit,
    private val dependencies: CategoryEditorDependencies,
) : BaseComponent<CategoryEditorState, CategoryEditorViewAction>(InitialCategoryEditorState),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val snackbarManager = snackbarManager()

    init {
        if (dependencies.args.id != -1) {
            getCategory()
        }
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

    private fun onSaveClick() {
        validateUserInput {
            reduceState { copy(buttonIsLoading = true) }
            coroutineScope.launch {
                val result = if (dependencies.args.id != -1) {
                    dependencies.categoryRepository.updateCategory(
                        category = Category(
                            id = state.value.id,
                            name = state.value.name,
                            color = state.value.selectedColor,
                            iconId = state.value.selectedIcon,
                            isTemplate = false,
                        )
                    )
                } else {
                    dependencies.categoryRepository.createCategory(
                        category = Category(
                            id = 0,
                            name = state.value.name,
                            color = state.value.selectedColor,
                            iconId = state.value.selectedIcon,
                            isTemplate = false
                        )
                    )
                }

                result.fold(
                    onSuccess = {
                        reduceState {
                            copy(buttonIsLoading = false)
                        }
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

    private fun onUpdateIcon(icon: String) {
        reduceState { copy(selectedIcon = icon) }
    }

    private fun getCategory() {
        coroutineScope.launch {
            reduceState { copy(buttonIsLoading = true) }

            val result = dependencies.categoryRepository.getCategoryById(id = dependencies.args.id)

            result.fold(
                onSuccess = {
                    reduceState {
                        copy(
                            id = it.id,
                            name = it.name,
                            selectedIcon = it.iconId,
                            selectedColor = it.color,
                            buttonIsLoading = false
                        )
                    }
                },
                onFailure = {
                    snackbarManager.showSnackbar(it.localizedMessage ?: "")
                    reduceState { copy(buttonIsLoading = false) }
                }
            )
        }
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