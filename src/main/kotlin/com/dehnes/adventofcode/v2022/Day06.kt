package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day06 {

    @Test
    fun run() {
        val line = getLines().first()
        check(firstDistinct(line, 4) == 1816)
        check(firstDistinct(line, 14) == 2625)
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