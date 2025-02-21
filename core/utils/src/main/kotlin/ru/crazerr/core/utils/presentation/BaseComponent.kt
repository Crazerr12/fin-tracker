package ru.crazerr.core.utils.presentation

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update

abstract class BaseComponent<State : Any, ViewAction>(
    initialState: State
) {
    private val _state: MutableValue<State> = MutableValue(initialState)
    val state: Value<State> = _state

    abstract fun handleViewAction(action: ViewAction)

    protected fun reduceState(reducer: State.() -> State) {
        _state.update { it.reducer() }
    }
}