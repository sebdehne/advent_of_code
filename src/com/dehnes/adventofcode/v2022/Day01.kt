package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import java.io.File

class Day01 {

    val input = File("resources/2022/day01.txt").readLines()

    @Test
    fun run() {
        val calories = mutableListOf<Long>()

        var current = 0L
        input.forEach { line ->
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

        println("Part1: ${top.entries.first().value}")
        println("Part2: ${top.values.sum()}")
    }
}