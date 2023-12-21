package org.dda.waveformeditor.domain

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.dda.waveformeditor.domain.entities.WaveData
import java.io.OutputStream

interface WaveDataFormat {

    suspend fun parse(fileData: String, validateValues: Boolean): Result<WaveData>

    suspend fun save(data: WaveData, selection: IntRange, output: OutputStream): Result<Int>

}

class WaveDataFormatImpl(
    private val cellDelimiter: String = " ",
) : WaveDataFormat {

    override suspend fun parse(fileData: String, validateValues: Boolean): Result<WaveData> {
        return withContext(Dispatchers.Default) {
            try {
                Result.success(
                    WaveData(
                        validateValues = validateValues,
                        values = fileData.lineSequence().filter { line ->
                            line.isNotBlank()
                        }.flatMap { line ->
                            line.split(cellDelimiter).map { value ->
                                println("value: $value -> ${value.toDouble()}")
                                value.toDouble()
                            }
                        }.toImmutableList().toDoubleArray()
                    )
                )
            } catch (e: Throwable) {
                Result.failure(IllegalArgumentException("Wrong data format"))
            }
        }
    }

    override suspend fun save(
        data: WaveData,
        selection: IntRange,
        output: OutputStream
    ): Result<Int> {
        return withContext(Dispatchers.IO) {
            runCatching {
                var lines = 0
                output.bufferedWriter().use { writer ->
                    data.iteratePairsIndexed(
                        fromPairIndex = selection.first,
                        toPairIndex = selection.last,
                    ) { _, top, bottom ->
                        writer.appendLine("$bottom$cellDelimiter$top")
                        lines++
                    }
                }
                lines
            }
        }
    }

}