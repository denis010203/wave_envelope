package org.dda.waveformeditor.ui.screens.wave_editor.render

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import org.dda.waveformeditor.domain.entities.WaveData
import org.dda.waveformeditor.ui.theme.WaveformEditorTheme
import kotlin.math.roundToInt


@Composable
fun WaveEditor(
    modifier: Modifier,
    data: WaveData,
    style: WaveStyle,
    startSelector: WaveSelector.Start,
    endSelector: WaveSelector.End,
    selectedIdxStart: Int = 0,
    selectedIdxEnd: Int = data.pairsCount - 1,
    minSelectionGap: Dp = 5.dp,
    isShowDebug: Boolean = false,
    onSelect: (startIndex: Int, endIndex: Int) -> Unit,
) {
    val selectionGapPx = with(LocalDensity.current) { minSelectionGap.toPx() }
    val endSelectorWidthPx = with(LocalDensity.current) {
        (endSelector.bulbSize + endSelector.lineWidth).toPx()
    }

    fun WaveSelector.indexToPx(
        index: Int,
        layoutInfo: WaveLayoutInfo,
    ): Float {
        val selectorShift = when (this) {
            is WaveSelector.Start -> 0F
            is WaveSelector.End -> -endSelectorWidthPx
        }
        val stepX = layoutInfo.waveBounds.width / (data.pairsCount - 1)
        return stepX * index + layoutInfo.waveBounds.left + selectorShift
    }

    fun WaveLayoutInfo.onSelectOffset(
        offsetStartX: Float,
        offsetEndX: Float,
    ) {
        val stepX = waveBounds.width / (data.pairsCount - 1)
        val startIndex = (offsetStartX / stepX).roundToInt()
        val endIndex = (offsetEndX / stepX).roundToInt()
        onSelect(startIndex, endIndex)
    }

    Box(
        modifier = modifier
    ) {

        val selectorPaddingValues = PaddingValues(
            top = startSelector.bulbSize / 2,
            bottom = endSelector.bulbSize / 2,
        )

        var offsetStartX by remember { mutableFloatStateOf(Float.NaN) }
        var offsetEndX by remember { mutableFloatStateOf(Float.NaN) }
        var layoutInfo: WaveLayoutInfo? by remember(
            data,
            startSelector,
            endSelector
        ) { mutableStateOf(null) }

        Wave(
            modifier = Modifier
                .matchParentSize()
                .padding(selectorPaddingValues)
                .onGloballyPositioned { layoutCoordinates ->
                    layoutInfo = WaveLayoutInfo(
                        waveBounds = layoutCoordinates.boundsInParent(),
                    ).also { info ->
                        if (offsetStartX.isNaN()) {
                            offsetStartX = startSelector.indexToPx(selectedIdxStart, info)
                        }
                        if (offsetEndX.isNaN()) {
                            offsetEndX = endSelector.indexToPx(selectedIdxEnd, info)
                        }
                    }
                },
            data = data,
            style = style,
            isShowDebug = isShowDebug,
        )

        layoutInfo?.run {
            var isStartHighlighted by remember(startSelector) {
                mutableStateOf(false)
            }
            var isEndHighlighted by remember(endSelector) {
                mutableStateOf(false)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(selectorPaddingValues)
                    .offset {
                        IntOffset(
                            (offsetStartX - waveBounds.width - waveBounds.left).roundToInt(),
                            0
                        )
                    }
                    .background(
                        color = startSelector.selectionColor
                    )
            ) {}
            Selector(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset { IntOffset(offsetStartX.roundToInt(), 0) }
                    .onDragSelector(
                        onStart = {
                            isStartHighlighted = true
                        },
                        onStop = {
                            isStartHighlighted = false
                        },
                        onDrag = { dragAmount ->
                            val nextValue = offsetStartX + dragAmount
                            if (nextValue in waveBounds.left..offsetEndX) {
                                offsetStartX = nextValue
                                if (offsetEndX < offsetStartX + selectionGapPx) {
                                    offsetEndX = offsetStartX + selectionGapPx
                                }
                                onSelectOffset(offsetStartX, offsetEndX)
                                true
                            } else {
                                false
                            }
                        }
                    ),
                selector = startSelector,
                isHighlighted = isStartHighlighted,
            )
            Selector(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset { IntOffset(offsetEndX.roundToInt(), 0) }
                    .onDragSelector(
                        onStart = {
                            isEndHighlighted = true
                        },
                        onStop = {
                            isEndHighlighted = false
                        },
                        onDrag = { dragAmount ->
                            val nextValue = offsetEndX + dragAmount
                            if (nextValue in offsetStartX..(waveBounds.right - endSelector.bulbSize.toPx())) {
                                offsetEndX = nextValue
                                if (offsetEndX < offsetStartX + selectionGapPx) {
                                    offsetStartX = offsetEndX - selectionGapPx
                                }
                                onSelectOffset(offsetStartX, offsetEndX)
                                true
                            } else {
                                false
                            }
                        }
                    ),
                selector = endSelector,
                isHighlighted = isEndHighlighted,
            )
            Box(
                modifier = Modifier
                    .offset { IntOffset((offsetEndX + endSelectorWidthPx).roundToInt(), 0) }
                    .fillMaxSize()
                    .padding(selectorPaddingValues)
                    .background(
                        color = startSelector.selectionColor
                    )
            ) {}
        }
    }
}

@Immutable
sealed interface WaveSelector {

    val color: Color
    val highlightColor: Color
    val selectionColor: Color
    val lineWidth: Dp
    val bulbSize: Dp

    fun getColor(isHighlight: Boolean): Color {
        return if (isHighlight) highlightColor else color
    }

    @Immutable
    data class Start(
        override val color: Color,
        override val highlightColor: Color,
        override val selectionColor: Color,
        override val lineWidth: Dp,
        override val bulbSize: Dp,
    ) : WaveSelector

    @Immutable
    data class End(
        override val color: Color,
        override val highlightColor: Color,
        override val selectionColor: Color,
        override val lineWidth: Dp,
        override val bulbSize: Dp,
    ) : WaveSelector
}

private data class WaveLayoutInfo(
    val waveBounds: Rect,
)

@Composable
private fun Selector(
    modifier: Modifier,
    selector: WaveSelector,
    isHighlighted: Boolean,
    isShowDebug: Boolean = false,
) {
    val selectorColor = selector.getColor(isHighlighted)
    val minTouchSize = 48.dp
    Box(
        modifier = modifier
            .widthIn(min = max(selector.bulbSize, minTouchSize))
            .let {
                if (isShowDebug) {
                    it.background(Color.Cyan)
                } else {
                    it
                }
            }
    ) {
        Canvas(
            modifier = Modifier.fillMaxHeight()
        ) {

            val bulbSizePx = selector.bulbSize.toPx()
            val bulbHalfSizePx = bulbSizePx / 2F

            translate(
                left = when (selector) {
                    is WaveSelector.Start -> 0F
                    is WaveSelector.End -> bulbSizePx
                }
            ) {
                drawRect(
                    color = selectorColor,
                    size = Size(
                        width = (selector.lineWidth).toPx(),
                        height = size.height
                    ),
                )

                when (selector) {
                    is WaveSelector.Start -> {
                        drawRect(
                            color = selectorColor,
                            size = Size(
                                width = bulbHalfSizePx,
                                height = (selector.bulbSize).toPx(),
                            ),
                        )
                        drawArc(
                            color = selectorColor,
                            startAngle = -90F,
                            sweepAngle = 180F,
                            useCenter = true,
                            size = Size(
                                width = bulbSizePx,
                                height = bulbSizePx,
                            )
                        )
                    }

                    is WaveSelector.End -> {
                        drawRect(
                            topLeft = Offset(
                                x = -bulbHalfSizePx,
                                y = size.height - bulbSizePx,
                            ),
                            color = selectorColor,
                            size = Size(
                                width = bulbHalfSizePx,
                                height = bulbSizePx,
                            ),
                        )
                        drawArc(
                            topLeft = Offset(
                                x = -bulbSizePx,
                                y = size.height - bulbSizePx,
                            ),
                            color = selectorColor,
                            startAngle = 90F,
                            sweepAngle = 180F,
                            useCenter = true,
                            size = Size(
                                width = bulbSizePx,
                                height = bulbSizePx,
                            )
                        )
                    }
                }
            }

        }
    }
}

fun Modifier.onDragSelector(
    onStart: () -> Unit,
    onStop: () -> Unit,
    onDrag: PointerInputScope.(dragAmount: Float) -> Boolean,
): Modifier {
    return pointerInput("onDragSelector") {
        awaitEachGesture {
            awaitFirstDown()
            onStart()
            val up = waitForUpOrCancellation()
            if (up != null) {
                onStop()
            }
        }
    }.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = {
                onStart()
            },
            onDragCancel = {
                onStop()
            },
            onDragEnd = {
                onStop()
            }
        ) { change, dragAmount ->
            if (onDrag(dragAmount)) {
                change.consume()
            }
        }
    }
}


@Composable
@Preview
private fun WaveEditorPreview() {
    WaveformEditorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = rememberScrollState()
                ),
        ) {
            val data = WaveData(
                values = doubleArrayOf(
                    -0.24105835, 0.21847534,
                    -0.24606323, 0.17340088,
                    -0.035339355, 0.04449463,
                    -0.018493652, 0.010772705,
                    -0.1473999, 0.17605591,
                    -0.32095337, 0.28915405,
                    -0.17208862, 0.15939331,
                    -0.08102417, 0.10876465,
                    -0.018615723, 0.023864746,
                )
            )
            var selected: Pair<Int, Int>? by remember {
                mutableStateOf(null)
            }
            WaveEditor(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                data = data,
                selectedIdxStart = 3,
                selectedIdxEnd = 7,
                style = WaveStyle.FilledLines(
                    fillColor = Color.Yellow,
                    lineWidth = 2.dp,
                    lineTopColor = Color.Magenta,
                    lineBottomColor = Color.Cyan,
                ),
                startSelector = WaveSelector.Start(
                    color = Color.Blue,
                    highlightColor = Color.Red,
                    selectionColor = Color.Blue.copy(alpha = 0.5F),
                    lineWidth = 4.dp,
                    bulbSize = 20.dp,
                ),
                endSelector = WaveSelector.End(
                    color = Color.Blue,
                    highlightColor = Color.Red,
                    selectionColor = Color.Blue.copy(alpha = 0.5F),
                    lineWidth = 4.dp,
                    bulbSize = 20.dp,
                ),
                isShowDebug = true,
            ) { startIndex, endIndex ->
                selected = startIndex to endIndex
            }
            selected?.run {
                data.iteratePairsIndexed { index, top, bottom ->
                    if (index in first..second) {
                        Text(text = "[$index, $top, $bottom]")
                    }
                }
            }
        }
    }
}
