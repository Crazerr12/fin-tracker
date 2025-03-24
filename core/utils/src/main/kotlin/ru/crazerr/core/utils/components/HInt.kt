package ru.crazerr.core.utils.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
fun Hint(value: String, style: TextStyle = MaterialTheme.typography.titleSmall) {
    Text(
        text = value,
        style = style,
    )
}