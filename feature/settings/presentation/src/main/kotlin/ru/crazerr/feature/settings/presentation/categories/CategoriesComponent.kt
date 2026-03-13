package ru.crazerr.feature.settings.presentation.categories

import androidx.paging.cachedIn
import androidx.paging.map
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.crazerr.core.utils.presentation.BaseComponent
import ru.crazerr.core.utils.presentation.componentCoroutineScope
import ru.crazerr.feature.category.presentation.api.mapper.toCategoryUi

class CategoriesComponent(
    componentContext: ComponentContext,
    private val onAction: (CategoriesComponentAction) -> Unit,
    private val dependencies: CategoriesDependencies,
) : BaseComponent<CategoriesState, CategoriesViewAction>(CategoriesState()),
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()

    init {
        getCategories()
        handleInput(dependencies.args.input)
    }

    override fun handleViewAction(action: CategoriesViewAction) {
        when (action) {
            CategoriesViewAction.BackClick -> onAction(CategoriesComponentAction.BackClick)
            CategoriesViewAction.CreateNewCategory -> onAction(CategoriesComponentAction.CreateNewCategory)
            is CategoriesViewAction.DeleteCategory -> onDeleteCategory(categoryId = action.categoryId)
            is CategoriesViewAction.EditExistCategory -> onAction(
                CategoriesComponentAction.EditExistCategory(
                    categoryUi = action.categoryUi,
                )
            )
        }
    }

    private fun getCategories() {
        val categoriesFlow = dependencies.categoryRepository.getCategories()
            .map { it.map { it.toCategoryUi() } }
            .cachedIn(coroutineScope)
        reduceState { copy(categories = categoriesFlow) }
    }

    private fun onDeleteCategory(categoryId: Long) {
        coroutineScope.launch {

        }
    }

    private fun handleInput(input: SharedFlow<Input>) {
        coroutineScope.launch {
            input.collectLatest { lastInput ->
                when (lastInput) {
                    is Input.CategoryInput -> getCategories()
                }
            }
        }
    }

    sealed interface Input {
        class CategoryInput() : Input
    }
}