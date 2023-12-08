package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.v2019.lcm
import org.junit.jupiter.api.Test
import java.io.File


class Day08 {

    val text = File("resources/2023/day08.txt").readText()
    val instructions = text.split("\n\n")[0]
    val data = text.split("\n\n")[1].lines().associate { line ->
        val (l, r) = line.split("=").map { it.trim() }
        val (left, right) = r.split(",").map { it.replace("(", "").replace(")", "").trim() }
        l to (left to right)
    }

    @Test
    fun run() {
        // part1
        val part1 = findSteps("AAA") { it == "ZZZ" }
        check(part1 == 18113L)

        // part2
        val part2 = data.entries
            .filter { it.key.endsWith("A") }
            .map { it.key }
            .map { findSteps(it) { it.endsWith("Z") } }
            .fold(1L) { acc, f -> lcm(acc, f) }

        check(part2 == 12315788159977L)
    }

    private fun findSteps(start: String, endReached: (str: String) -> Boolean): Long {
        var steps = 0L
        var current = start
        var instructionIndex = 0
        while (true) {
            if (endReached(current)) break

            if (instructionIndex == instructions.length) {
                instructionIndex = 0
            }
            val next = data[current]!!
            current = if (instructions[instructionIndex++] == 'R') next.second else next.first
            steps++
        }

        return steps
    }

}