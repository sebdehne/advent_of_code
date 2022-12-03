package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day03 {

    @Test
    fun run() {
        val rucksacks = inputLines(3)

        val priority = { c: Char ->
            if (c >= 'a') {
                1 + (c - 'a')
            } else {
                27 + (c - 'A')
            }
        }

        val part1 = rucksacks.sumOf { line ->
            val (left, right) = line.chunked(line.length / 2)
            priority(left.first { it in right })
        }

        val part2 = rucksacks.chunked(3).sumOf { gr ->
            priority(gr[0].first { it in gr[1] && it in gr[2] })
        }

        //expectThat(part1) isEqualTo 157
        //expectThat(part2) isEqualTo 70
        expectThat(part1) isEqualTo 7826
        expectThat(part2) isEqualTo 2577
    }
}