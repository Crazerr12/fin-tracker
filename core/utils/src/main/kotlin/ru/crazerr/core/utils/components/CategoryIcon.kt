package ru.crazerr.core.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun CategoryIcon(modifier: Modifier = Modifier, color: Long, icon: Any) {
    Box(
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(
                color = Color(color).copy(alpha = 0.15f),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.size(20.dp),
            model = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = Color(color))
        )
    }
}