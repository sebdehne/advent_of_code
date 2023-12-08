package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day14 {

    @Test
    fun run() {
        expectThat(
            getScan().let { (grid, highestY) ->
                runSimulation(highestY, grid, false)
            }
        ) isEqualTo 1298

        expectThat(
            getScan().let { (grid, highestY) ->
                runSimulation(highestY, grid, true)
            }
        ) isEqualTo 25585
    }

    fun getScan(): Pair<Array<CharArray>, Int> {
        val grid = Array(300) { CharArray(1000) { '.' } }
        var highestY = 0

        val addLine = { from: Pair<Int, Int>, to: Pair<Int, Int> ->
            val yRange = if (from.second < to.second) {
                from.second..to.second
            } else {
                to.second..from.second
            }
            val xRange = if (from.first < to.first) {
                from.first..to.first
            } else {
                to.first..from.first
            }
            yRange.forEach { y ->
                xRange.forEach { x ->
                    grid[y][x] = '#'
                    if (y > highestY) {
                        highestY = y
                    }
                }
            }
        }

        getLines().forEach { line ->
            line.split(" -> ").windowed(2, 1).forEach { (from, to) ->
                addLine(
                    from.split(",").let { (l, r) -> l.toInt() to r.toInt() },
                    to.split(",").let { (l, r) -> l.toInt() to r.toInt() }
                )
            }
        }

        return grid to highestY
    }

    private fun runSimulation(
        highestY: Int,
        grid: Array<CharArray>,
        isPart2: Boolean
    ): Int {
        val sandStart = (0 to 500)
        val sandAtRest = mutableListOf<Pair<Int, Int>>()
        val sandPossibleSteps = listOf(
            1 to 0,
            1 to -1,
            1 to 1
        )

        var done = false
        while (!done) {
            var nextSand = sandStart

            while (!done) {
                var moved = false
                for (candidateMove in sandPossibleSteps) {
                    val candidate = nextSand + candidateMove
                    if (candidate.first == (highestY + 2)) {
                        if (isPart2) {
                            continue
                        } else {
                            done = true
                            break
                        }
                    }
                    if (grid[candidate.first][candidate.second] == '.') {
                        nextSand = candidate
                        moved = true
                        break
                    }
                }
                if (!done && !moved) {
                    grid[nextSand.first][nextSand.second] = 'o'
                    sandAtRest.add(nextSand)
                    if (nextSand == sandStart) {
                        done = true
                    }
                    break
                }
            }
        }
        return sandAtRest.size
    }

}