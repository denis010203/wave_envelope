package org.dda.waveformeditor.ui.screens.wave_editor.render

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorState

@Composable
fun RenderStateLoaded(
    modifier: Modifier,
    state: WaveEditorState.Loaded,
    onSelect: (startIndex: Int, endIndex: Int) -> Unit,
    onSwitchDebug: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(
            state = rememberScrollState()
        )
    ) {
        val colorList = MaterialTheme.colorScheme.getColorMap()
            .values
            .take(15)
            .toSet()
        var waveFillColor by remember { mutableStateOf(Color.Yellow) }
        var lineTopColor by remember { mutableStateOf(Color.Magenta) }
        var lineBottomColor by remember { mutableStateOf(Color.Cyan) }
        var selectorColor by remember { mutableStateOf(Color.Blue) }
        var selectorHighlightColor by remember { mutableStateOf(Color.Red) }

        WaveEditor(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            data = state.waveEditData.waveData,
            selectedIdxStart = state.waveEditData.selectionStartPairIndex,
            selectedIdxEnd = state.waveEditData.selectionEndPairIndex,
            style = WaveStyle.FilledLines(
                fillColor = waveFillColor,
                lineWidth = 3.dp,
                lineTopColor = lineTopColor,
                lineBottomColor = lineBottomColor,
            ),
            startSelector = WaveSelector.Start(
                color = selectorColor,
                highlightColor = selectorHighlightColor,
                selectionColor = selectorColor.copy(alpha = 0.5F),
                lineWidth = 4.dp,
                bulbSize = 20.dp,
            ),
            endSelector = WaveSelector.End(
                color = selectorColor,
                highlightColor = selectorHighlightColor,
                selectionColor = selectorColor.copy(alpha = 0.5F),
                lineWidth = 4.dp,
                bulbSize = 20.dp,
            ),
            isShowDebug = state.showDebug,
            onSelect = onSelect
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .align(Alignment.End)
        ) {
            Text(
                modifier = Modifier.padding(end = 10.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = "Enable debug",
            )
            Switch(
                checked = state.showDebug,
                onCheckedChange = onSwitchDebug,
            )
        }
        RenderColorSelector(
            label = "Wave fill color",
            color = waveFillColor,
            colorList = colorList
        ) { color ->
            waveFillColor = color
        }
        RenderColorSelector(
            label = "Top envelope color",
            color = lineTopColor,
            colorList = colorList
        ) { color ->
            lineTopColor = color
        }
        RenderColorSelector(
            label = "Bottom envelope color",
            color = lineBottomColor,
            colorList = colorList
        ) { color ->
            lineBottomColor = color
        }
        RenderColorSelector(
            label = "Selector color",
            color = selectorColor,
            colorList = colorList
        ) { color ->
            selectorColor = color
        }
        RenderColorSelector(
            label = "Selector highlight color",
            color = selectorHighlightColor,
            colorList = colorList
        ) { color ->
            selectorHighlightColor = color
        }
        if (state.showDebug) {
            Text(text = "Selected data:")
            state.waveEditData.iteratePairsSelected { index, top, bottom ->
                Text(text = "[$index, $top, $bottom]")
            }

            Text(
                text = "Loaded data:"
            )
            state.waveEditData.waveData.iteratePairsIndexed { index, top, bottom ->
                Text(text = "[$index, $top, $bottom]")
            }
        }
    }
}

@Composable
private fun ColumnScope.RenderColorSelector(
    label: String,
    color: Color,
    colorList: Set<Color>,
    onValueChange: (color: Color) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .align(Alignment.Start)
    ) {
        var colorPickerOpen by rememberSaveable { mutableStateOf(false) }
        Button(
            modifier = Modifier.padding(end = 10.dp),
            onClick = {
                colorPickerOpen = !colorPickerOpen
            }
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        shape = CircleShape,
                        color = color
                    )
            ) {}
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.headlineSmall,
                text = label,
            )
        }
        if (colorPickerOpen) {
            PickColorDialog(
                colorList = colorList,
                selected = color,
                onDismiss = {
                    colorPickerOpen = false
                },
                onColorSelected = { color ->
                    onValueChange(color)
                }
            )
        }
    }
}