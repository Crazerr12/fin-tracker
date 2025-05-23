package ru.crazerr.feature.category.presentation.categoryEditor

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.category.presentation.R
import ru.crazerr.feature.domain.api.Category
import ru.crazerr.feature.icon.domain.api.IconModel

class CategoryEditorComponent(
    componentContext: ComponentContext,
    private val onAction: (CategoryEditorComponentAction) -> Unit,
    private val dependencies: CategoryEditorDependencies,
) : BaseComponent<CategoryEditorState, CategoryEditorViewAction>(InitialCategoryEditorState),
    ComponentContext by componentContext {
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
            is CategoryEditorViewAction.UpdateIcon -> onUpdateIcon(iconModel = action.iconModel)
            is CategoryEditorViewAction.UpdateName -> onUpdateName(name = action.name)
        }
    }

    private fun getInitData() {
        coroutineScope.launch {
            reduceState { copy(buttonIsLoading = true) }
            val iconsResult = dependencies.iconRepository.getIcons()

            iconsResult.fold(
                onSuccess = { reduceState { copy(selectedIconModel = it[0], icons = it) } },
                onFailure = { snackbarManager.showSnackbar(it.localizedMessage ?: "") },
            )

            if (dependencies.args.id != -1L) {
                getCategory()
            }

            reduceState { copy(buttonIsLoading = false) }
        }
    }

    private fun onSaveClick() {
        validateUserInput {
            reduceState { copy(buttonIsLoading = true) }
            coroutineScope.launch {
                val result = if (dependencies.args.id != -1L) {
                    dependencies.categoryRepository.updateCategory(
                        category = Category(
                            id = state.value.id,
                            name = state.value.name,
                            color = state.value.selectedColor,
                            iconModel = state.value.selectedIconModel,
                            isTemplate = false,
                        )
                    )
                } else {
                    dependencies.categoryRepository.createCategory(
                        category = Category(
                            id = 0,
                            name = state.value.name,
                            color = state.value.selectedColor,
                            iconModel = state.value.selectedIconModel,
                            isTemplate = false
                        )
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

    private fun onUpdateIcon(iconModel: IconModel) {
        reduceState { copy(selectedIconModel = iconModel) }
    }

    private suspend fun getCategory() {
        reduceState { copy(buttonIsLoading = true) }

        val result = dependencies.categoryRepository.getCategoryById(id = dependencies.args.id)

        result.fold(
            onSuccess = {
                reduceState {
                    copy(
                        id = it.id,
                        name = it.name,
                        selectedIconModel = it.iconModel,
                        selectedColor = it.color,
                    )
                }
            },
            onFailure = {
                snackbarManager.showSnackbar(it.localizedMessage ?: "")
            }
        )
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