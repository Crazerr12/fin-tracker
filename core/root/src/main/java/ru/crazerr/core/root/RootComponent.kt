package ru.crazerr.core.root

import android.R.attr.value
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

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    val selectedBottomNavigationItem: Value<BottomNavigationItem>
    val snackbarManager: SnackbarManager

    fun obtainBottomNavigation(item: BottomNavigationItem)

    fun isBottomNavigationVisible(): Boolean

    sealed interface Child {
        class AuthStory() : Child
        class MainStory() : Child
        class AnalysisStory() : Child
        class BudgetStory() : Child
        class ProfileStory() : Child
        class TransactionsStory() : Child
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
        data object BudgetStoryConfig : Config

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
            BottomNavigationItem.Budget -> navigation.pushToFront(RootComponent.Config.BudgetStoryConfig)
            BottomNavigationItem.Profile -> navigation.pushToFront(RootComponent.Config.ProfileStoryConfig)
        }
        _selectedBottomNavigationItem.value = item
    }

    override fun isBottomNavigationVisible(): Boolean {
        return when (stack.active.instance) {
            is RootComponent.Child.MainStory,
            is RootComponent.Child.TransactionsStory,
            is RootComponent.Child.AnalysisStory,
            is RootComponent.Child.BudgetStory,
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
            RootComponent.Config.BudgetStoryConfig -> createBudgetStory(componentContext = componentContext)
            RootComponent.Config.MainStoryConfig -> createMainStory(componentContext = componentContext)
            RootComponent.Config.ProfileStoryConfig -> createProfileStory(componentContext = componentContext)
            RootComponent.Config.TransactionsStoryConfig -> createTransactionsStory(componentContext = componentContext)
        }
    }

    private fun createMainStory(componentContext: ComponentContext): RootComponent.Child.MainStory =
        RootComponent.Child.MainStory()

    private fun createAuthStory(componentContext: ComponentContext): RootComponent.Child.AuthStory =
        RootComponent.Child.AuthStory()

    private fun createAnalysisStory(componentContext: ComponentContext): RootComponent.Child.AnalysisStory =
        RootComponent.Child.AnalysisStory()

    private fun createTransactionsStory(componentContext: ComponentContext): RootComponent.Child.TransactionsStory =
        RootComponent.Child.TransactionsStory()

    private fun createProfileStory(componentContext: ComponentContext): RootComponent.Child.ProfileStory =
        RootComponent.Child.ProfileStory()

    private fun createBudgetStory(componentContext: ComponentContext): RootComponent.Child.BudgetStory =
        RootComponent.Child.BudgetStory()

    internal class FactoryImpl() : RootComponent.Factory {
        override fun create(componentContext: ComponentContext) =
            RootComponentImpl(componentContext = componentContext)
    }
}