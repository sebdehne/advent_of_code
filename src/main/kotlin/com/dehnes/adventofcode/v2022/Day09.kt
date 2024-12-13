package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.minus
import com.dehnes.adventofcode.utils.plus
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day09 {

    @Test
    fun run() {
        check(runSimulation(2) == 6236)
        check(runSimulation(10) == 2449)
    }

    private fun runSimulation(knots: Int): Int {
        val p = (0 until knots).map { 0 to 0 }.toTypedArray()

        val visited = mutableSetOf<Pair<Int, Int>>()
        visited.add(p[0])

        getLines().forEach { line ->
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

    private fun Pair<Int, Int>.limit() = (if (this.first > 1) 1 else if (this.first < -1) -1 else this.first) to
            if (this.second > 1) 1 else if (this.second < -1) -1 else this.second

}

