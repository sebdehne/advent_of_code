package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day06 {

    @Test
    fun run() {
        val line = inputLines(6).first()
        expectThat(findMarker2(line, 4)) isEqualTo 1816
        expectThat(findMarker2(line, 14)) isEqualTo 2625
    }

    private fun findMarker2(line: String, limit: Int): Int {
        var count = limit
        var reslt = -1
        line.windowed(limit, 1) {
            if (reslt == -1 && it.toList().distinct().size == limit) {
                reslt = count
            }
            count++
        }
        return reslt
    }
}