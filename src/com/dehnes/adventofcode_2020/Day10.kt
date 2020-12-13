package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day10 {
    val adapters = File("resources/day10.txt").readLines().map { it.toInt() }.sorted()
    val jolts = adapters.let {
        listOf(0) + it + (it.last() + 3)
    }

    @Test
    fun run() {
        val deltas = jolts.windowed(2) {
            val (a, b) = it
            (b - a) to b
        }.groupingBy { it.first }.eachCount()

        println("Part1: " + deltas[1]!! * deltas[3]!!) // 2263

        var count = 0
        var product = 1L
        jolts.forEachIndexed { index, jolt ->
            if (jolt - (jolts.getOrElse(index - 1) { 0 }) > 1) {
                product *= when {
                    count < 3 -> 1
                    count == 3 -> 2
                    count == 4 -> 4
                    count == 5 -> 7
                    else -> error("")
                }
                count = 0
            }
            count++
        }

        println("Part2: $product") // 396857386627072
    }

}