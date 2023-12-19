package org.dda.waveformeditor.ui.screens.wave_editor.render

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.dda.waveformeditor.domain.entities.WaveData
import org.dda.waveformeditor.ui.theme.WaveformEditorTheme

@Composable
fun Wave(
    modifier: Modifier,
    data: WaveData,
    style: WaveStyle,
    isShowDebug: Boolean = false,
) {
    Surface(modifier = modifier.testTag("wave_chart")) {
        Box(
            modifier = Modifier.clipToBounds()
        ) {
            val textMeasure: TextMeasurer? = if (isShowDebug) {
                rememberTextMeasurer()
            } else {
                null
            }

            Canvas(
                modifier = Modifier
                    .matchParentSize(),
                onDraw = {
                    if (data.isNotEmpty()) {
                        when (style) {
                            is WaveStyle.Filled -> drawFilled(
                                data = data,
                                style = style,
                            )

                            is WaveStyle.FilledLines -> drawFilledLines(
                                data = data,
                                style = style,
                            )

                            is WaveStyle.Lines -> drawLines(
                                data = data,
                                style = style,
                            )
                        }
                        if (isShowDebug) {
                            drawDebugInfo(
                                textMeasure = textMeasure,
                                data = data
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
@Preview
private fun WaveDataPreview() {
    WaveformEditorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = rememberScrollState()
                ),
        ) {
            val data = WaveData(
                values = floatArrayOf(
                    0.0F, 0.0F,
                    -0.6F, 0.5F,
                    -0.1F, 0.2F,
                    -0.9F, 1.0F,
                    -0.3F, 0.4F,
                )
            )
            Wave(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(start = 40.dp),
                data = data,
                style = WaveStyle.Filled(Color.Red),
                isShowDebug = true,
            )
            Wave(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                data = data,
                style = WaveStyle.FilledLines(
                    fillColor = Color.Yellow,
                    lineWidth = 3.dp,
                    lineTopColor = Color.Magenta,
                    lineBottomColor = Color.Cyan,
                ),
                isShowDebug = true,
            )
            Wave(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                data = data,
                style = WaveStyle.Lines(
                    width = 1.dp,
                    lineTopColor = Color.Green,
                    lineBottomColor = Color.Blue,
                ),
                isShowDebug = true,
            )
        }
    }
}

@Immutable
sealed interface WaveStyle {

    @Immutable
    data class Filled(
        val fillColor: Color,
        val alpha: Float = 1F,
    ) : WaveStyle

    @Immutable
    data class Lines(
        val width: Dp,
        val lineTopColor: Color,
        val alpha: Float = 1F,
        val lineBottomColor: Color = lineTopColor,
    ) : WaveStyle

    @Immutable
    data class FilledLines(
        val fillColor: Color,
        val lineWidth: Dp,
        val lineTopColor: Color,
        val alpha: Float = 1F,
        val lineBottomColor: Color = lineTopColor,
    ) : WaveStyle
}

private fun DrawScope.drawFilled(
    data: WaveData,
    style: WaveStyle.Filled,
) {
    drawPath(
        path = data.generatePath(center, size),
        color = style.fillColor,
        style = Fill,
        alpha = style.alpha,
    )
}

private fun DrawScope.drawLines(
    data: WaveData,
    style: WaveStyle.Lines,
) {
    drawPath(
        path = data.generateTopPath(center, size),
        color = style.lineTopColor,
        style = Stroke(
            width = style.width.toPx()
        ),
        alpha = style.alpha,
    )
    drawPath(
        path = data.generateBottomPath(center, size),
        color = style.lineBottomColor,
        style = Stroke(
            width = style.width.toPx()
        ),
        alpha = style.alpha,
    )
}

private fun DrawScope.drawFilledLines(
    data: WaveData,
    style: WaveStyle.FilledLines,
) {
    drawPath(
        path = data.generatePath(center, size),
        color = style.fillColor,
        style = Fill,
        alpha = style.alpha,
    )
    drawPath(
        path = data.generateTopPath(center, size),
        color = style.lineTopColor,
        style = Stroke(
            width = style.lineWidth.toPx()
        ),
        alpha = style.alpha,
    )
    drawPath(
        path = data.generateBottomPath(center, size),
        color = style.lineBottomColor,
        style = Stroke(
            width = style.lineWidth.toPx()
        ),
        alpha = style.alpha,
    )
}

private fun DrawScope.drawDebugInfo(
    textMeasure: TextMeasurer?,
    data: WaveData
) {
    if (textMeasure != null) {
        val stepX = size.width / (data.pairsCount - 1)
        var x = 0F
        data.iteratePairsIndexed { index, top, bottom ->
            drawText(
                textMeasurer = textMeasure,
                text = "[$index, $top]",
                topLeft = Offset(
                    y = translateY(top, center),
                    x = x
                )
            )
            drawText(
                textMeasurer = textMeasure,
                text = "[$index, $bottom]",
                topLeft = Offset(
                    y = translateY(bottom, center),
                    x = x
                )
            )
            x += stepX
        }
    }
}

private fun WaveData.generateTopPath(center: Offset, size: Size): Path {
    val path = Path()
    val stepX = size.width / (pairsCount - 1)
    var x = 0F
    iterateTopIndexed { index, top ->
        if (index == 0) {
            path.moveTo(x = x, y = translateY(top, center))
        } else {
            path.lineTo(x = x, y = translateY(top, center))
        }
        x += stepX
    }
    return path
}

private fun WaveData.generateBottomPath(center: Offset, size: Size): Path {
    val path = Path()
    val stepX = size.width / (pairsCount - 1)
    var x = 0F
    iterateBottomIndexed { index, bottom ->
        if (index == 0) {
            path.moveTo(x = x, y = translateY(bottom, center))
        } else {
            path.lineTo(x = x, y = translateY(bottom, center))
        }
        x += stepX
    }
    return path
}


private fun WaveData.generatePath(center: Offset, size: Size): Path {
    return generateTopPath(center, size).apply {
        lineTo(
            x = size.width,
            y = translateY(lastBottom(), center)
        )

        val stepX = size.width / (pairsCount - 1)
        var x = size.width
        iterateBottomIndexed(reversed = true) { _, bottom ->
            lineTo(
                x = x,
                y = translateY(bottom, center)
            )
            x -= stepX
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun translateY(y: Float, center: Offset): Float {
    return -y * center.y + center.y
}