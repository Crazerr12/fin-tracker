package ru.crazerr.fintracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.inject
import ru.crazerr.core.root.RootComponent
import ru.crazerr.core.root.RootCoordinator
import ru.crazerr.fintracker.ui.theme.FinTrackerTheme

class MainActivity : ComponentActivity() {
    private val rootComponentFactory: RootComponent.Factory by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.navigationBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()

        enableEdgeToEdge()
        setContent {
            FinTrackerTheme {
                RootCoordinator(
                    component = rootComponentFactory.create(componentContext = defaultComponentContext())
                )
            }
        }
    }
}
