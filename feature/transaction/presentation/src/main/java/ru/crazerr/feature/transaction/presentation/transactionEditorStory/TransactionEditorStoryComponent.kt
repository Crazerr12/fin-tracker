package ru.crazerr.feature.transaction.presentation.transactionEditorStory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import ru.crazerr.feature.account.presentation.AccountEditorArgs
import ru.crazerr.feature.account.presentation.AccountEditorComponent
import ru.crazerr.feature.account.presentation.AccountEditorComponentAction
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorArgs
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponent
import ru.crazerr.feature.category.presentation.categoryEditor.CategoryEditorComponentAction
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorArgs
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponent
import ru.crazerr.feature.transaction.presentation.transactionEditor.TransactionEditorComponentAction

class TransactionEditorStoryComponent(
    componentContext: ComponentContext,
    private val onAction: (TransactionEditorStoryComponentAction) -> Unit,
    private val dependencies: TransactionEditorStoryDependencies,
) : ComponentContext by componentContext {

    private val input =
        MutableSharedFlow<TransactionEditorComponent.Input>(extraBufferCapacity = Int.MAX_VALUE)

    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.TransactionEditor(),
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun onBackClick() {
        navigation.pop()
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.AccountEditor -> createAccountEditor(componentContext = componentContext)
            is Config.CategoryEditor -> createCategoryEditor(componentContext = componentContext)
            is Config.TransactionEditor -> createTransactionEditor(componentContext = componentContext)
        }

    private fun createAccountEditor(
        componentContext: ComponentContext
    ): Child.AccountEditor =
        Child.AccountEditor(
            component = dependencies.accountEditorComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        AccountEditorComponentAction.BackClick -> onBackClick()
                        is AccountEditorComponentAction.SaveAccount -> {
                            input.tryEmit(TransactionEditorComponent.Input.AccountInput(account = action.account))
                            onBackClick()
                        }
                    }
                },
                args = AccountEditorArgs()
            )
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createTransactionEditor(
        componentContext: ComponentContext,
    ): Child.TransactionEditor = Child.TransactionEditor(
        component = dependencies.transactionEditorComponentFactory.create(
            componentContext = componentContext,
            onAction = { action ->
                when (action) {
                    TransactionEditorComponentAction.BackClick -> onAction(
                        TransactionEditorStoryComponentAction.BackClick
                    )

                    TransactionEditorComponentAction.CreateNewAccount -> navigation.push(Config.AccountEditor)
                    TransactionEditorComponentAction.CreateNewCategory -> navigation.push(Config.CategoryEditor)
                    is TransactionEditorComponentAction.SavedClick -> onAction(
                        TransactionEditorStoryComponentAction.TransactionCreated(transaction = action.transaction)
                    )
                }
            },
            args = TransactionEditorArgs(input = input, id = dependencies.args.transactionId)
        )
    )

    private fun createCategoryEditor(
        componentContext: ComponentContext,
    ): Child.CategoryEditor = Child.CategoryEditor(
        component = dependencies.categoryEditorComponentFactory.create(
            componentContext = componentContext,
            onAction = { action ->
                when (action) {
                    CategoryEditorComponentAction.BackClick -> onBackClick()
                    is CategoryEditorComponentAction.SaveClick -> {
                        input.tryEmit(TransactionEditorComponent.Input.CategoryInput(category = action.category))
                        onBackClick()
                    }
                }
            },
            args = CategoryEditorArgs()
        )
    )

    sealed interface Child {
        class TransactionEditor(val component: TransactionEditorComponent) : Child
        class AccountEditor(val component: AccountEditorComponent) : Child
        class CategoryEditor(val component: CategoryEditorComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data class TransactionEditor(val id: Int = -1) : Config

        @Serializable
        data object AccountEditor : Config

        @Serializable
        data object CategoryEditor : Config
    }
}