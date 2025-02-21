package ru.crazerr.core.utils.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Hint(value: String) {
    Text(
        text = value,
        style = MaterialTheme.typography.titleSmall,
    )
}