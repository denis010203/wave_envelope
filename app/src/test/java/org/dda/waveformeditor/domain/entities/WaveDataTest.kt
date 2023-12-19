package org.dda.waveformeditor.domain.entities

import org.junit.Test
import kotlin.test.assertEquals

class WaveDataTest {

    @Test
    fun `Check parsing`() {
        val stringData = """
            -0.00579834 0.0082092285
            -0.009674072 0.009429932
            -0.008850098 0.0077209473
            -0.007171631 0.007293701
            -0.007659912 0.0069885254
            -0.24105835 0.21847534
            -0.24606323 0.17340088
            
        """.trimIndent()
        val waveData = stringData.parseToWaveData().getOrNull()
        assertEquals(
            actual = waveData,
            expected = WaveData(
                values = floatArrayOf(
                    -0.00579834F, 0.0082092285F,
                    -0.009674072F, 0.009429932F,
                    -0.008850098F, 0.0077209473F,
                    -0.007171631F, 0.007293701F,
                    -0.007659912F, 0.0069885254F,
                    -0.24105835F, 0.21847534F,
                    -0.24606323F, 0.17340088F,
                )
            )
        )
    }

    @Test
    fun `Check iteratePairs`() {
        val actual = mutableListOf<Triple<Int, Float, Float>>()
        WaveData(
            values = floatArrayOf(
                -0.00579834F, 0.0082092285F,
                -0.009674072F, 0.009429932F,
                -0.008850098F, 0.0077209473F,
                -0.007171631F, 0.007293701F,
                -0.007659912F, 0.0069885254F,
                -0.24105835F, 0.21847534F,
                -0.24606323F, 0.17340088F,
            )
        ).iteratePairsIndexed { index, top, bottom ->
            actual.add(Triple(index, bottom, top))
        }
        assertEquals(
            actual = actual,
            expected = mutableListOf(
                Triple(0, -0.00579834F, 0.0082092285F),
                Triple(1, -0.009674072F, 0.009429932F),
                Triple(2, -0.008850098F, 0.0077209473F),
                Triple(3, -0.007171631F, 0.007293701F),
                Triple(4, -0.007659912F, 0.0069885254F),
                Triple(5, -0.24105835F, 0.21847534F),
                Triple(6, -0.24606323F, 0.17340088F),
            )
        )
    }

    @Test
    fun `Check iterateBottomIndexed reversed`() {
        val actual = mutableListOf<Pair<Int, Float>>()
        WaveData(
            values = floatArrayOf(
                -0.00579834F, 0.0082092285F,
                -0.009674072F, 0.009429932F,
                -0.008850098F, 0.0077209473F,
                -0.007171631F, 0.007293701F,
                -0.007659912F, 0.0069885254F,
                -0.24105835F, 0.21847534F,
                -0.24606323F, 0.17340088F,
            )
        ).iterateBottomIndexed(reversed = true) { index, bottom ->
            actual.add(index to bottom)
        }
        assertEquals(
            actual = actual,
            expected = mutableListOf(
                6 to -0.24606323F,
                5 to -0.24105835F,
                4 to -0.007659912F,
                3 to -0.007171631F,
                2 to -0.008850098F,
                1 to -0.009674072F,
                0 to -0.00579834F,
            )
        )
    }
}