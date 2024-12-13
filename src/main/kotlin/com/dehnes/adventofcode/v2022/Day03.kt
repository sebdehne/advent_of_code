package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day03 {

    @Test
    fun run() {
        val rucksacks = getLines()

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

        //check(part1== 157L)
        //check(part2== 70L)
        check(part1 == 7826)
        check(part2 == 2577)
    }
}