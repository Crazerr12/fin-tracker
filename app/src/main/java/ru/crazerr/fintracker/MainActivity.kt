package ru.crazerr.fintracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.inject
import ru.crazerr.core.root.RootComponent
import ru.crazerr.core.root.RootCoordinator
import ru.crazerr.fintracker.ui.theme.FinTrackerTheme

class MainActivity : ComponentActivity() {
    private val rootComponentFactory: RootComponent.Factory by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent = rootComponentFactory.create(defaultComponentContext())
        enableEdgeToEdge()
        setContent {
            FinTrackerTheme {
                RootCoordinator(
                    component = rootComponent,
                )
            }
        }
    }
}
