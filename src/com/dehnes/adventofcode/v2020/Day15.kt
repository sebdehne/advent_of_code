package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test

class Day15 {

    @Test
    fun test() {
        val start = listOf(0L, 1, 4, 13, 15, 12, 16)

        println(play(start, 2020)) // 1665
        println(play(start, 30000000)) // 16439
    }

    private fun play(start: List<Long>, stopAt: Long) = start
            .mapIndexed { index, l -> l to listOf(index + 1L) }.toMap()
            .toMutableMap().let { spokenAt ->
                (start.size + 1..stopAt).fold(start.last()) { prev, turn ->
                    val turns = spokenAt[prev]
                    val newV = if (turns?.size != 2) 0 else turns[0] - turns[1]
                    spokenAt[newV] = listOfNotNull(turn, spokenAt[newV]?.getOrNull(0))
                    newV
                }
            }
}