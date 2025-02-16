package ru.crazerr.core.utils.snackbar

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.compose.getKoin
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SnackbarManager {
    private val _snackbarState = MutableStateFlow<SnackbarData?>(null)
    val snackbarState = _snackbarState.asStateFlow()

    fun showSnackbar(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        _snackbarState.value =
            SnackbarData(message = message, actionLabel = actionLabel, onAction = onAction)
    }

    fun dismissSnackbar() {
        _snackbarState.value = null
    }
}

data class SnackbarData(
    val message: String,
    val actionLabel: String?,
    val onAction: (() -> Unit)?,
)

fun ComponentContext.snackbarManager(): SnackbarManager {
    return object : KoinComponent {
        val manager: SnackbarManager by inject()
    }.manager
}