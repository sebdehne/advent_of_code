package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day01 {

    @Test
    fun run() {
        val calories = mutableListOf<Long>()

        var current = 0L
        getLines().forEach { line ->
            val cal = line.trim().ifBlank { null }?.toLong()
            if (cal == null) {
                calories.add(current)
                current = 0
            } else {
                current += cal
            }
        }
        calories.add(current)

        val top = mutableMapOf<Int, Long>()
        repeat(3) {
            val max = calories.filterIndexed { index, _ -> index !in top }.max()
            top[calories.indexOfFirst { it == max }] = max
        }

        // part - 1
        check(68923L == top.entries.first().value)

        // part - 2
        check(200044L == top.values.sum())
    }
}