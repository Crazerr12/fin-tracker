package ru.crazerr.core.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.crazerr.core.utils.snackbar.SnackbarManager
import ru.crazerr.core.utils.snackbar.snackbarManager
import ru.crazerr.feature.budgets.presentation.budgetsStory.BudgetsStoryComponent
import ru.crazerr.feature.main.presentation.mainStory.MainStoryComponent
import ru.crazerr.feature.transactions.presentation.transactionsStory.TransactionsStoryComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    val selectedBottomNavigationItem: Value<BottomNavigationItem>
    val snackbarManager: SnackbarManager

    fun obtainBottomNavigation(item: BottomNavigationItem)

    fun isBottomNavigationVisible(): Boolean

    sealed interface Child {
        class AuthStory() : Child
        class MainStory(val component: MainStoryComponent) : Child
        class TransactionsStory(val component: TransactionsStoryComponent) : Child
        class AnalysisStory() : Child
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
        data object TransactionsStoryConfig : Config
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

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = RootComponent.Config.serializer(),
        initialConfiguration = RootComponent.Config.MainStoryConfig,
        handleBackButton = true,
        childFactory = ::child,
    )

    private val _selectedBottomNavigationItem: MutableValue<BottomNavigationItem> = MutableValue(
        BottomNavigationItem.Main
    )
    override val selectedBottomNavigationItem: Value<BottomNavigationItem>
        get() = _selectedBottomNavigationItem

    override val snackbarManager: SnackbarManager = snackbarManager()


    override fun obtainBottomNavigation(item: BottomNavigationItem) {
        when (item) {
            BottomNavigationItem.Main -> navigation.pushToFront(RootComponent.Config.MainStoryConfig)
            BottomNavigationItem.Transactions -> navigation.pushToFront(RootComponent.Config.TransactionsStoryConfig)
            BottomNavigationItem.Analysis -> navigation.pushToFront(RootComponent.Config.AnalysisStoryConfig)
            BottomNavigationItem.Budget -> navigation.pushToFront(RootComponent.Config.BudgetsStoryConfig)
            BottomNavigationItem.Profile -> navigation.pushToFront(RootComponent.Config.ProfileStoryConfig)
        }
        _selectedBottomNavigationItem.value = item
    }

    override fun isBottomNavigationVisible(): Boolean {
        return when (stack.active.instance) {
            is RootComponent.Child.MainStory,
            is RootComponent.Child.TransactionsStory,
            is RootComponent.Child.AnalysisStory,
            is RootComponent.Child.BudgetsStory,
            is RootComponent.Child.ProfileStory -> true

            else -> false
        }
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
            RootComponent.Config.TransactionsStoryConfig -> createTransactionsStory(componentContext = componentContext)
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
        RootComponent.Child.AnalysisStory()

    private fun createTransactionsStory(componentContext: ComponentContext): RootComponent.Child.TransactionsStory =
        RootComponent.Child.TransactionsStory(
            component = di.transactionsStoryComponentFactory.create(
                componentContext
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