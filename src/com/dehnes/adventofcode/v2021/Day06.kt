package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day06 {

    val input = File("resources/2021/day06.txt").readLines().first()
        .split(",").map { it.toInt() }

    @Test
    fun run() {
        assertEquals(386536, simulate(input, 80))
        assertEquals(1732821262171, simulate(input, 256))
    }

    private fun simulate(input: List<Int>, days: Int): Long {
        val fishCountByAge = LongArray(9)
        input.forEach { fishCountByAge[it]++ }

        for (day in 1..days) {
            val zeroDays = fishCountByAge[0]
            for (age in 1..8) {
                fishCountByAge[age - 1] = fishCountByAge[age]
            }
            fishCountByAge[6] += zeroDays
            fishCountByAge[8] = zeroDays
        }

        return fishCountByAge.sum()
    }
}