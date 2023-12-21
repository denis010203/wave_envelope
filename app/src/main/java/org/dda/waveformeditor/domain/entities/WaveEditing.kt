package org.dda.waveformeditor.domain.entities

data class WaveEditData(
    val waveData: WaveData,
    val selectionStartPairIndex: Int,
    val selectionEndPairIndex: Int,
) {
    init {
        require(waveData.isStartIndexValid(selectionStartPairIndex))
        require(waveData.isEndIndexValid(selectionEndPairIndex))
    }

    constructor(waveData: WaveData) : this(
        waveData = waveData,
        selectionStartPairIndex = 0,
        selectionEndPairIndex = waveData.pairsCount - 1
    )

    inline fun iteratePairsSelected(onElement: (index: Int, top: Double, bottom: Double) -> Unit) {
        waveData.iteratePairsIndexed(selectionStartPairIndex, selectionEndPairIndex, onElement)
    }
}

fun WaveData.isValidSelection(startPairIndex: Int, endPairIndex: Int): Boolean {
    return startPairIndex in indicesPairs && endPairIndex in indicesPairs
}

fun WaveData.isStartIndexValid(selectionStartPairIndex: Int): Boolean {
    return selectionStartPairIndex in indicesPairs
}

fun WaveData.isEndIndexValid(selectionStartPairIndex: Int): Boolean {
    return selectionStartPairIndex in indicesPairs
}
