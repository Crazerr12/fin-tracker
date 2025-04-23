package ru.crazerr.core.utils.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update


class NavigationManager {
    private val _bottomBar: MutableValue<Boolean> = MutableValue(false)
    val bottomBar: Value<Boolean> = _bottomBar

    internal fun showBottomBar() {
        _bottomBar.update { true }
    }

    internal fun hideBottomBar() {
        _bottomBar.update { false }
    }
}

fun ComponentContext.showBottomBar() {
    navigationManager().showBottomBar()
}

fun ComponentContext.hideBottomBar() {
    navigationManager().hideBottomBar()
}