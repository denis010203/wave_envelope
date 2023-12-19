package org.dda.waveformeditor.ui.screens.wave_editor.render

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PickColorDialog(
    colorList: List<Color>,
    selected: Color,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    val gridState = rememberLazyGridState()

    AlertDialog(
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.outline,
        onDismissRequest = onDismiss,
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState
            ) {
                colorList.forEach { color ->
                    item(
                        key = color.value.toString()
                    ) {
                        Box(modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                width = if (selected == color) {
                                    2.dp
                                } else {
                                    0.dp
                                },
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(color)
                            .requiredSize(70.dp)
                            .clickable {
                                onColorSelected(color)
                                onDismiss()
                            }
                        ) {
                        }
                    }
                }
            }
        },
        confirmButton = {}
    )
}

fun ColorScheme.getColorMap(): Map<String, Color> {
    return mapOf(
        "primary" to primary,
        "onPrimary" to onPrimary,
        "primaryContainer" to primaryContainer,
        "onPrimaryContainer" to onPrimaryContainer,
        "inversePrimary" to inversePrimary,
        "secondary" to secondary,
        "onSecondary" to onSecondary,
        "secondaryContainer" to secondaryContainer,
        "onSecondaryContainer" to onSecondaryContainer,
        "tertiary" to tertiary,
        "onTertiary" to onTertiary,
        "tertiaryContainer" to tertiaryContainer,
        "onTertiaryContainer" to onTertiaryContainer,
        "background" to background,
        "onBackground" to onBackground,
        "surface" to surface,
        "onSurface" to onSurface,
        "surfaceVariant" to surfaceVariant,
        "onSurfaceVariant" to onSurfaceVariant,
        "surfaceTint" to surfaceTint,
        "inverseSurface" to inverseSurface,
        "inverseOnSurface" to inverseOnSurface,
        "error" to error,
        "onError" to onError,
        "errorContainer" to errorContainer,
        "onErrorContainer" to onErrorContainer,
        "outline" to outline,
        "outlineVariant" to outlineVariant,
        "scrim" to scrim,
    )
}