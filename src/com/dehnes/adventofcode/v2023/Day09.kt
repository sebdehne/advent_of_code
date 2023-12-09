package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day09 {

    @Test
    fun run() {
        val part1 = getLines()
            .map { it.split(" ").map { it.toLong() } }
            .sumOf { it.last() + calc(it, true) }
        check(part1 == 1974913025L)

        val part2 = getLines()
            .map { it.split(" ").map { it.toLong() } }
            .sumOf { it.first() - calc(it, false) }
        check(part2 == 884L)
    }

    private fun calc(list: List<Long>, doEnd: Boolean): Long {
        if (list.all { it == 0L }) return 0

        val newList = mutableListOf<Long>()
        list.windowed(2, 1) {
            val (l, r) = it
            newList.add(r - l)
        }

        return if (doEnd) {
            newList.last() + calc(newList, doEnd)
        } else {
            newList.first() - calc(newList, doEnd)
        }
    }

}