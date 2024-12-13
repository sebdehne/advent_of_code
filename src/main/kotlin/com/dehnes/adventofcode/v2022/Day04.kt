package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day04 {

    @Test
    fun run() {
        var part1 = 0L
        var part2 = 0L

        getLines().forEach { line ->
            val sections = line.split(",", "-").map { it.toInt() }
            val p1 = sections[0]..sections[1]
            val p2 = sections[2]..sections[3]
            part1 += if (p1.containedBy(p2) || p2.containedBy(p1)) 1L else 0L
            part2 += if (p1.overlaps(p2) || p2.overlaps(p1)) 1L else 0L
        }

        expectThat(part1) isEqualTo 424
        expectThat(part2) isEqualTo 804
    }

    fun IntRange.containedBy(other: IntRange) = this.first in other && this.last in other
    fun IntRange.overlaps(other: IntRange) = this.first in other || this.last in other
}