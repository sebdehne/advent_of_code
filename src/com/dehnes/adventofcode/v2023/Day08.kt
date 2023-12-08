package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.MathUtils.lcm
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
        check(findSteps("AAA") { it == "ZZZ" } == 18113L)

        // part2
        check(data.entries
            .mapNotNull { if (it.key.endsWith("A")) findSteps(it.key) { it.endsWith("Z") } else null }
            .fold(1L) { acc, f -> lcm(acc, f) } == 12315788159977L)
    }

    private fun findSteps(start: String, endReached: (str: String) -> Boolean): Long {
        var steps = 0L
        var current = start
        var i = 0
        while (true) {
            if (i == instructions.length) {
                i = 0
            }
            current = data[current]!!.let {
                if (instructions[i++] == 'R') it.second else it.first
            }
            steps++
            if (endReached(current)) return steps
        }
    }

}