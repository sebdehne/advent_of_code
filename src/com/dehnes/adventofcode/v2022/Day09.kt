package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.math.absoluteValue

class Day09 {

    @Test
    fun run() {
        expectThat(runSimulation(2)) isEqualTo 6236
        expectThat(runSimulation(10)) isEqualTo 2449
    }

    private fun runSimulation(knots: Int): Int {
        val p = (0 until knots).map { 0 to 0 }.toTypedArray()

        val visited = mutableSetOf<Pair<Int, Int>>()
        visited.add(p[0])

        inputLines(9).forEach { line ->
            val (direction, steps) = line.split(" ")

            repeat(steps.toInt()) {
                p[0] += directionToStep(direction)

                (1 until knots).forEach { i ->
                    if (p[i - 1].isInProximity(p[i])) {
                        return@repeat
                    }

                    p[i] += (p[i - 1] - p[i]).limit()
                }
                visited.add(p.last())
            }
        }
        return visited.size
    }

    private fun directionToStep(dir: String) = when (dir) {
        "U" -> 0 to -1
        "D" -> 0 to 1
        "R" -> 1 to 0
        "L" -> -1 to 0
        else -> error("")
    }

    private fun Pair<Int, Int>.isInProximity(other: Pair<Int, Int>) =
        (this.first - other.first).absoluteValue in 0..1 && (this.second - other.second).absoluteValue in 0..1

    private operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
    private operator fun Pair<Int, Int>.minus(o: Pair<Int, Int>) = (this.first - o.first) to (this.second - o.second)
    private fun Pair<Int, Int>.limit() = (if (this.first > 1) 1 else if (this.first < -1) -1 else this.first) to
            if (this.second > 1) 1 else if (this.second < -1) -1 else this.second

}

