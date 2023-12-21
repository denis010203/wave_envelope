package org.dda.waveformeditor.domain.entities

import org.junit.Test
import kotlin.test.assertEquals

class WaveDataTest {

    @Test
    fun `Check iteratePairs`() {
        val actual = mutableListOf<Triple<Int, Double, Double>>()
        WaveData(
            values = doubleArrayOf(
                -0.00579834, 0.0082092285,
                -0.009674072, 0.009429932,
                -0.008850098, 0.0077209473,
                -0.007171631, 0.007293701,
                -0.007659912, 0.0069885254,
                -0.24105835, 0.21847534,
                -0.24606323, 0.17340088,
            )
        ).iteratePairsIndexed { index, top, bottom ->
            actual.add(Triple(index, bottom, top))
        }
        assertEquals(
            actual = actual,
            expected = mutableListOf(
                Triple(0, -0.00579834, 0.0082092285),
                Triple(1, -0.009674072, 0.009429932),
                Triple(2, -0.008850098, 0.0077209473),
                Triple(3, -0.007171631, 0.007293701),
                Triple(4, -0.007659912, 0.0069885254),
                Triple(5, -0.24105835, 0.21847534),
                Triple(6, -0.24606323, 0.17340088),
            )
        )
    }

    @Test
    fun `Check iterateBottomIndexed reversed`() {
        val actual = mutableListOf<Pair<Int, Double>>()
        WaveData(
            values = doubleArrayOf(
                -0.00579834, 0.0082092285,
                -0.009674072, 0.009429932,
                -0.008850098, 0.0077209473,
                -0.007171631, 0.007293701,
                -0.007659912, 0.0069885254,
                -0.24105835, 0.21847534,
                -0.24606323, 0.17340088,
            )
        ).iterateBottomIndexed(reversed = true) { index, bottom ->
            actual.add(index to bottom)
        }
        assertEquals(
            actual = actual,
            expected = mutableListOf(
                6 to -0.24606323,
                5 to -0.24105835,
                4 to -0.007659912,
                3 to -0.007171631,
                2 to -0.008850098,
                1 to -0.009674072,
                0 to -0.00579834,
            )
        )
    }
}