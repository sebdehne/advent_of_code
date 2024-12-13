package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day06 {

    val input = getLines().first().split(",").map { it.toInt() }

    @Test
    fun run() {
        check(simulate(input, 80) == 386536L)
        check(simulate(input, 256) == 1732821262171L)
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