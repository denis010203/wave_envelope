package org.dda.waveformeditor.domain.entities

import androidx.compose.runtime.Immutable

@Immutable
class WaveData(
    private val values: FloatArray
) {
    init {
        require(values.size % 2 == 0)
    }

    private val hash by lazy { values.contentHashCode() }

    fun getAtIndex(index: Int): Float = values[index]

    val indicesAll: IntRange
        get() = values.indices

    val pairsCount: Int = values.size / 2

    val indicesPairs: IntRange = IntRange(0, pairsCount)

    fun getTop(index: Int): Float = values[index + 1]
    fun getBottom(index: Int): Float = values[index]

    fun lastTop(): Float {
        return values.last()
    }

    fun lastBottom(): Float {
        if (isEmpty())
            throw NoSuchElementException("Array is empty.")
        return values[values.size - 2]
    }

    fun firstTop(): Float {
        return values.first()
    }

    fun isEmpty(): Boolean = values.isEmpty()
    fun isNotEmpty(): Boolean = values.isNotEmpty()

    inline fun iteratePairsIndexed(onElement: (index: Int, top: Float, bottom: Float) -> Unit) {
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
        onElement: (index: Int, top: Float, bottom: Float) -> Unit
    ) {
        for (i in fromPairIndex..toPairIndex step 2) {
            onElement(
                i / 2,
                getAtIndex(i + 1),
                getAtIndex(i)
            )
        }
    }

    inline fun iterateTopIndexed(onElement: (index: Int, top: Float) -> Unit) {
        for (i in indicesAll step 2) {
            onElement(
                i / 2,
                getAtIndex(i + 1)
            )
        }
    }

    inline fun iterateBottomIndexed(
        reversed: Boolean = false,
        onElement: (index: Int, bottom: Float) -> Unit
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

fun String.parseToWaveData(
    delimiters: String = " "
): Result<WaveData> {
    return try {
        Result.success(
            WaveData(
                values = lines().filter { line ->
                    line.isNotBlank()
                }.flatMap { line ->
                    line.split(delimiters).map { value ->
                        value.toFloat()
                    }
                }.toFloatArray()
            )
        )
    } catch (e: Throwable) {
        Result.failure(IllegalArgumentException("Wrong data format"))
    }
}