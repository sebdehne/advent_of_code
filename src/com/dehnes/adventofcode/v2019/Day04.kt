package com.dehnes.adventofcode.v2019

import org.junit.jupiter.api.Test

class Day04 {

    val input = 265275..781584

    @Test
    fun main() {
        check(input.count { candidate ->
            candidate.duplicates().maxOf { it.value.size } > 1 && candidate.increasesOnly()
        } == 960)

        check(input.count { candidate ->
            candidate.duplicates().any { it.value.size == 2 } && candidate.increasesOnly()
        } == 626)
    }

    fun Int.duplicates() = this.toString().toList().groupBy { it }
    fun Int.increasesOnly() = this.toString().let { it.toList().sorted() == it.toList() }

}