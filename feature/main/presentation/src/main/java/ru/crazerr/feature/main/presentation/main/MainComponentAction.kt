package ru.crazerr.feature.main.presentation.main

sealed interface MainComponentAction {
    data class GoToAccount(val id: Long = -1) : MainComponentAction
}