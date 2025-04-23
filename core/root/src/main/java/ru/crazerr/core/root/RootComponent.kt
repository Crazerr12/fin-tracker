package ru.crazerr.core.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable
import ru.crazerr.core.utils.navigation.navigationManager
import ru.crazerr.core.utils.serializators.LocalDateSerializer
import ru.crazerr.core.utils.snackbar.SnackbarManager
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.analysis.presentation.analysisStory.AnalysisStoryComponent
import ru.crazerr.feature.analysis.presentation.analysisStory.AnalysisStoryComponentAction
import ru.crazerr.feature.budgets.presentation.budgetsStory.BudgetsStoryComponent
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponent
import ru.crazerr.feature.transaction.domain.api.TransactionType
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryArgs
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryComponent
import java.time.LocalDate
import kotlin.system.exitProcess

interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>
    val selectedBottomNavigationItem: Value<BottomNavigationItem>
    val snackbarManager: SnackbarManager
    val bottomNavigation: Value<Boolean>

    fun obtainBottomNavigation(item: BottomNavigationItem)

    sealed interface Child {
        class AuthStory() : Child
        class MainStory(val component: MainStoryComponent) : Child
        class TransactionsStory(val component: TransactionsStoryComponent) : Child
        class AnalysisStory(val component: AnalysisStoryComponent) : Child
        class BudgetsStory(val component: BudgetsStoryComponent) : Child
        class ProfileStory() : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object AuthStoryConfig : Config

        @Serializable
        data object MainStoryConfig : Config

        @Serializable
        data object AnalysisStoryConfig : Config

        @Serializable
        data object BudgetsStoryConfig : Config

        @Serializable
        data object ProfileStoryConfig : Config

        @Serializable
        data class TransactionsStoryConfig(
            val categoryIds: LongArray? = null,
            val accountIds: LongArray? = null,
            @Serializable(with = LocalDateSerializer::class)
            val startDate: LocalDate? = null,
            @Serializable(with = LocalDateSerializer::class)
            val endDate: LocalDate? = null,
            val transactionType: TransactionType = TransactionType.Income,
        ) : Config
    }

    interface Factory {
        fun create(componentContext: ComponentContext): RootComponent
    }
}

internal class RootComponentImpl(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, RootComponent {
    private val navigation = StackNavigation<RootComponent.Config>()

    private val di: DiInjector by lazy {
        DiInjector.create()
    }

    private val navigationManager = navigationManager()

    override val bottomNavigation: Value<Boolean>
        get() = navigationManager.bottomBar

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = RootComponent.Config.serializer(),
        initialConfiguration = RootComponent.Config.MainStoryConfig,
        handleBackButton = false,
        childFactory = ::child,
    )

    private val _selectedBottomNavigationItem: MutableValue<BottomNavigationItem> = MutableValue(
        BottomNavigationItem.Main
    )
    override val selectedBottomNavigationItem: Value<BottomNavigationItem>
        get() = _selectedBottomNavigationItem

    override val snackbarManager: SnackbarManager = snackbarManager()

    private val backCallback = BackCallback {
        if (stack.value.backStack.isNotEmpty()) navigation.pop() else exitProcess(0)
        when (stack.value.active.configuration) {
            RootComponent.Config.MainStoryConfig -> {
                _selectedBottomNavigationItem.value = BottomNavigationItem.Main
            }

            RootComponent.Config.TransactionsStoryConfig -> {
                _selectedBottomNavigationItem.value = BottomNavigationItem.Transactions
            }

            RootComponent.Config.BudgetsStoryConfig -> {
                _selectedBottomNavigationItem.value = BottomNavigationItem.Budget
            }

            RootComponent.Config.AnalysisStoryConfig -> {
                _selectedBottomNavigationItem.value = BottomNavigationItem.Analysis
            }
        }
    }

    init {
        backHandler.register(backCallback)
    }

    override fun obtainBottomNavigation(item: BottomNavigationItem) {
        when (item) {
            BottomNavigationItem.Main -> navigation.pushToFront(RootComponent.Config.MainStoryConfig)
            BottomNavigationItem.Transactions -> navigation.pushToFront(RootComponent.Config.TransactionsStoryConfig())
            BottomNavigationItem.Analysis -> navigation.pushToFront(RootComponent.Config.AnalysisStoryConfig)
            BottomNavigationItem.Budget -> navigation.pushToFront(RootComponent.Config.BudgetsStoryConfig)
            // BottomNavigationItem.Profile -> navigation.pushToFront(RootComponent.Config.ProfileStoryConfig)
        }
        _selectedBottomNavigationItem.value = item
    }

    private fun child(
        config: RootComponent.Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            RootComponent.Config.AnalysisStoryConfig -> createAnalysisStory(componentContext = componentContext)
            RootComponent.Config.AuthStoryConfig -> createAuthStory(componentContext = componentContext)
            RootComponent.Config.BudgetsStoryConfig -> createBudgetsStory(componentContext = componentContext)
            RootComponent.Config.MainStoryConfig -> createMainStory(componentContext = componentContext)
            RootComponent.Config.ProfileStoryConfig -> createProfileStory(componentContext = componentContext)
            is RootComponent.Config.TransactionsStoryConfig -> createTransactionsStory(
                componentContext = componentContext,
                config = config,
            )
        }
    }

    private fun createMainStory(componentContext: ComponentContext): RootComponent.Child.MainStory =
        RootComponent.Child.MainStory(
            component = di.mainStoryComponentFactory.create(
                componentContext = componentContext
            )
        )

    private fun createAuthStory(componentContext: ComponentContext): RootComponent.Child.AuthStory =
        RootComponent.Child.AuthStory()

    private fun createAnalysisStory(componentContext: ComponentContext): RootComponent.Child.AnalysisStory =
        RootComponent.Child.AnalysisStory(
            component = di.analysisStoryComponentFactory.create(
                componentContext = componentContext,
                onAction = { action ->
                    when (action) {
                        is AnalysisStoryComponentAction.OnCategoryClick -> {
                            obtainBottomNavigation(BottomNavigationItem.Transactions)
                            navigation.pushToFront(
                                RootComponent.Config.TransactionsStoryConfig(
                                    categoryIds = longArrayOf(action.categoryId),
                                    startDate = action.startDate,
                                    endDate = action.endDate,
                                    transactionType = action.transactionType,
                                )
                            )
                        }
                    }
                }
            )
        )

    private fun createTransactionsStory(
        componentContext: ComponentContext,
        config: RootComponent.Config.TransactionsStoryConfig,
    ): RootComponent.Child.TransactionsStory =
        RootComponent.Child.TransactionsStory(
            component = di.transactionsStoryComponentFactory.create(
                componentContext = componentContext,
                args = TransactionsStoryArgs(
                    categoryIds = config.categoryIds,
                    accountIds = config.accountIds,
                    startDate = config.startDate,
                    endDate = config.endDate,
                    transactionType = config.transactionType,
                )
            )
        )

    private fun createProfileStory(componentContext: ComponentContext): RootComponent.Child.ProfileStory =
        RootComponent.Child.ProfileStory()

    private fun createBudgetsStory(componentContext: ComponentContext): RootComponent.Child.BudgetsStory =
        RootComponent.Child.BudgetsStory(
            component = di.budgetsStoryComponentFactory.create(
                componentContext = componentContext
            )
        )

    internal class FactoryImpl() : RootComponent.Factory {
        override fun create(componentContext: ComponentContext) =
            RootComponentImpl(componentContext = componentContext)
    }
}