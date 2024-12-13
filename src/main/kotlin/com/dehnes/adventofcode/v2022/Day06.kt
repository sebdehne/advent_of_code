package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day06 {

    @Test
    fun run() {
        val line = getLines().first()
        expectThat(firstDistinct(line, 4)) isEqualTo 1816
        expectThat(firstDistinct(line, 14)) isEqualTo 2625
    }

    private fun firstDistinct(line: String, limit: Int): Int {
        var count = limit
        var result = -1
        line.windowed(limit, 1) {
            if (result == -1 && it.toList().distinct().size == limit) {
                result = count
            }
            count++
        }
        return result
    }
}