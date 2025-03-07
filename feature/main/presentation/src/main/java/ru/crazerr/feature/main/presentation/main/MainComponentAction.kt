package ru.crazerr.feature.main.presentation.main

sealed interface MainComponentAction {
    data class GoToAccount(val id: Int = -1) : MainComponentAction
}