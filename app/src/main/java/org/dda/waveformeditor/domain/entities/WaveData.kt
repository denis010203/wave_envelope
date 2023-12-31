package org.dda.waveformeditor.domain.entities

import androidx.compose.runtime.Immutable

@Immutable
class WaveData(
    private val values: DoubleArray,
    validateValues: Boolean = false
) {
    companion object {
        val VALUE_RANGE = -1.0..1.0
    }

    init {
        require(values.size % 2 == 0)
        if (validateValues) {
            require(
                values.all { value ->
                    value in VALUE_RANGE
                }
            )
            iteratePairsIndexed { index, top, bottom ->
                require(top >= bottom) {
                    "Wrong value pairs at $index top: $top $bottom"
                }
            }
        }
    }

    private val hash by lazy { values.contentHashCode() }

    fun getAtIndex(index: Int): Double = values[index]

    val indicesAll: IntRange
        get() = values.indices

    val pairsCount: Int = values.size / 2

    val indicesPairs: IntRange = IntRange(0, pairsCount)

    fun getTop(index: Int): Double = values[index + 1]
    fun getBottom(index: Int): Double = values[index]

    fun lastTop(): Double {
        return values.last()
    }

    fun lastBottom(): Double {
        if (isEmpty())
            throw NoSuchElementException("Array is empty.")
        return values[values.size - 2]
    }

    fun firstTop(): Double {
        return values.first()
    }

    fun isEmpty(): Boolean = values.isEmpty()
    fun isNotEmpty(): Boolean = values.isNotEmpty()

    inline fun iteratePairsIndexed(onElement: (index: Int, top: Double, bottom: Double) -> Unit) {
        for (i in indicesAll step 2) {
            onElement(
                i / 2,
                getAtIndex(i + 1),
                getAtIndex(i)
            )
        }
    }

    inline fun iteratePairsIndexed(
        fromPairIndex: Int,
        toPairIndex: Int,
        onElement: (index: Int, top: Double, bottom: Double) -> Unit
    ) {
        for (i in fromPairIndex..toPairIndex) {
            onElement(
                i,
                getAtIndex(i * 2 + 1),
                getAtIndex(i * 2)
            )
        }
    }

    inline fun iterateTopIndexed(onElement: (index: Int, top: Double) -> Unit) {
        for (i in indicesAll step 2) {
            onElement(
                i / 2,
                getAtIndex(i + 1)
            )
        }
    }

    inline fun iterateBottomIndexed(
        reversed: Boolean = false,
        onElement: (index: Int, bottom: Double) -> Unit
    ) {
        val range = if (reversed) {
            (indicesAll step 2).reversed()
        } else {
            indicesAll step 2
        }
        for (i in range) {
            onElement(
                i / 2,
                getAtIndex(i)
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WaveData

        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        return hash
    }
}