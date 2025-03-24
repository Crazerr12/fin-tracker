package ru.crazerr.feature.transactions.presentation.transactionsStory

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
import ru.crazerr.core.utils.serializators.LocalDateSerializer
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryArgs
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryComponent
import ru.crazerr.feature.transaction.presentation.transactionEditorStory.TransactionEditorStoryComponentAction
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsArgs
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponent
import ru.crazerr.feature.transactions.presentation.transactions.TransactionsComponentAction
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterArgs
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponent
import ru.crazerr.feature.transactions.presentation.transactionsFilter.TransactionsFilterComponentAction
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryComponent.Config.*
import java.time.LocalDate

class TransactionsStoryComponent(
    componentContext: ComponentContext,
    private val dependencies: TransactionsStoryDependencies,
) : ComponentContext by componentContext {
    private val transactionsInput: MutableSharedFlow<TransactionsComponent.Input> =
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Transactions,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Transactions -> createTransactionsComponent(componentContext = componentContext)
            is TransactionsFilter -> createTransactionsFilterComponent(
                config = config,
                componentContext = componentContext
            )

            is TransactionEditorStory -> createTransactionEditorStory(
                config = config,
                componentContext = componentContext
            )
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun createTransactionsComponent(componentContext: ComponentContext): Child.Transactions =
        Child.Transactions(
            component = dependencies.transactionsComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        is TransactionsComponentAction.GoToFilter -> navigation.push(
                            TransactionsFilter(
                                accountIds = action.accountIds,
                                categoryIds = action.categoryIds,
                                isFilterEnabled = action.isFilterEnabled,
                                startDate = action.startDate,
                                endDate = action.endDate
                            )
                        )

                        is TransactionsComponentAction.OpenTransactionEditor -> navigation.push(
                            TransactionEditorStory(transactionId = action.id)
                        )
                    }
                },
                args = TransactionsArgs(inputFlow = transactionsInput)
            )
        )

    private fun createTransactionsFilterComponent(
        config: TransactionsFilter,
        componentContext: ComponentContext
    ): Child.TransactionsFilter = Child.TransactionsFilter(
        component = dependencies.transactionsFilterComponentFactory.create(
            componentContext = componentContext,
            onAction = { action ->
                when (action) {
                    TransactionsFilterComponentAction.BackClick -> onBackClick()
                    is TransactionsFilterComponentAction.SaveButtonClick -> {
                        transactionsInput.tryEmit(
                            TransactionsComponent.Input.Filter(
                                accountIds = action.accountIds,
                                categoryIds = action.categoryIds,
                                startDate = action.startDate,
                                endDate = action.endDate,
                                isFilterEnabled = action.isFilterEnabled
                            )
                        )
                        onBackClick()
                    }
                }
            },
            args = TransactionsFilterArgs(
                accountIds = config.accountIds,
                categoryIds = config.categoryIds,
                startDate = config.startDate,
                endDate = config.endDate,
                isFilterEnabled = config.isFilterEnabled
            )
        )
    )

    private fun createTransactionEditorStory(
        config: TransactionEditorStory,
        componentContext: ComponentContext
    ): Child.TransactionEditorStory =
        Child.TransactionEditorStory(
            component = dependencies.transactionEditorStoryComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        TransactionEditorStoryComponentAction.BackClick -> onBackClick()
                        is TransactionEditorStoryComponentAction.TransactionCreated -> {
                            onBackClick()
                        }
                    }
                },
                args = TransactionEditorStoryArgs(transactionId = config.transactionId)
            )
        )

    private fun onBackClick() = navigation.pop()

    @Serializable
    sealed interface Config {
        @Serializable
        data object Transactions : Config

        @Serializable
        data class TransactionsFilter(
            val accountIds: LongArray,
            val categoryIds: LongArray,
            val isFilterEnabled: Boolean,
            @Serializable(with = LocalDateSerializer::class)
            val startDate: LocalDate?,
            @Serializable(with = LocalDateSerializer::class)
            val endDate: LocalDate?
        ) : Config

        @Serializable
        data class TransactionEditorStory(val transactionId: Long) : Config
    }

    sealed interface Child {
        class Transactions(val component: TransactionsComponent) : Child
        class TransactionsFilter(val component: TransactionsFilterComponent) : Child
        class TransactionEditorStory(val component: TransactionEditorStoryComponent) : Child
    }
}