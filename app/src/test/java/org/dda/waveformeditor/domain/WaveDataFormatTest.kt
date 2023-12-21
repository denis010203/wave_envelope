package org.dda.waveformeditor.domain

import kotlinx.coroutines.test.runTest
import org.dda.waveformeditor.domain.entities.WaveData
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WaveDataFormatTest {

    private lateinit var waveDataFormat: WaveDataFormat

    @Before
    fun setup() {
        waveDataFormat = WaveDataFormatImpl()
    }

    @Test
    fun `Check parsing correct string`() = runTest {
        val stringData = """
            -0.00579834 0.0082092285
            -0.009674072 0.009429932
            -0.008850098 0.0077209473
            -0.007171631 0.007293701
            -0.007659912 0.0069885254
            -0.24105835 0.21847534
            -0.24606323 0.17340088
            -1 1
            
        """.trimIndent()

        val waveDataResult = waveDataFormat.parse(stringData, true)
        assertEquals(
            expected = WaveData(
                values = doubleArrayOf(
                    -0.00579834, 0.0082092285,
                    -0.009674072, 0.009429932,
                    -0.008850098, 0.0077209473,
                    -0.007171631, 0.007293701,
                    -0.007659912, 0.0069885254,
                    -0.24105835, 0.21847534,
                    -0.24606323, 0.17340088,
                    -1.0, 1.0
                )
            ),
            actual = waveDataResult.getOrNull()
        )
    }

    @Test
    fun `Check parsing corrupted string`() = runTest {

        val dataToWrongDelimiter = """
            -0.00579834 0.0082092285
            -0.009674072   0.009429932
            -0.008850098 0.0077209473
        """.trimIndent()
        assertTrue {
            waveDataFormat.parse(
                fileData = dataToWrongDelimiter,
                validateValues = true
            ).isFailure
        }

        val dataToNotNumber = """
            -0.00579834 0.0082092285
            -0.009674072 0.009429932
            -0.008850098 q
        """.trimIndent()
        assertTrue {
            waveDataFormat.parse(
                fileData = dataToNotNumber,
                validateValues = true
            ).isFailure
        }
    }

    @Test
    fun `Check out of range`() = runTest {
        val dataOutOfRangePositive = """
            -0.00579834 0.0082092285
            -0.009674072 0.009429932
            -0.008850098 1.0000000001
            -0.00579834 0.0082092285
            -0.009674072 0.009429932
        """.trimIndent()
        assertTrue {
            waveDataFormat.parse(
                fileData = dataOutOfRangePositive,
                validateValues = true
            ).isFailure
        }

        val dataOutOfRangeNegative = """
            -0.00579834 0.0082092285
            -0.009674072 0.009429932
            -1.000000001 0.0077209473
            -0.00579834 0.0082092285
            -0.009674072 0.009429932
        """.trimIndent()
        assertTrue {
            waveDataFormat.parse(
                fileData = dataOutOfRangeNegative,
                validateValues = true
            ).isFailure
        }
    }
}