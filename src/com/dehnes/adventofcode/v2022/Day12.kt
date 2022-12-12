package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day12 {

    @Test
    fun run() {
        val inputLines = inputLines(12)

        var startPos = 0 to 0
        var dst = 0 to 0
        val grid = Array(inputLines.size) { IntArray(inputLines[0].length) }
        inputLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val h = when (c) {
                    'S' -> {
                        startPos = y to x
                        'a'
                    }

                    'E' -> {
                        dst = y to x
                        'z'
                    }

                    else -> c
                }
                grid[y][x] = h - 'a'
            }
        }

        val allStartPositions = mutableListOf<Pair<Int, Int>>()
        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 0) {
                    allStartPositions.add(y to x)
                }
            }
        }

        var shortesStart = Int.MAX_VALUE
        allStartPositions.forEach { start ->
            val candidate = grid.shortestTo(dst, start)
            if (candidate < shortesStart) {
                shortesStart = candidate
            }
        }

        expectThat(grid.shortestTo(dst, startPos)) isEqualTo 437
        expectThat(shortesStart) isEqualTo 430
    }

    fun Array<IntArray>.shortestTo(dst: Pair<Int, Int>, start: Pair<Int, Int>): Int {
        val paths = mutableListOf<List<Pair<Int, Int>>>()
        paths.add(listOf(start))
        val visited = mutableSetOf<Pair<Int, Int>>()
        visited.add(start)

        while (true) {

            val copy = paths.toList()
            paths.clear()
            copy.forEach { p ->
                val pos = p.last()
                val candidates = this.possibleSteps(pos).filterNot { it in visited }
                if (candidates.any { it == dst }) {
                    return p.size
                }
                visited.addAll(candidates)
                candidates.forEach { c ->
                    paths.add((p + c).toMutableList())
                }
            }

            if (paths.isEmpty()) {
                return Int.MAX_VALUE
            }
        }
    }

    fun Array<IntArray>.possibleSteps(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
        val currentHeight = this[pos.first][pos.second]
        val result = mutableListOf<Pair<Int, Int>>()
        listOf(
            0 to 1,
            0 to -1,
            1 to 0,
            -1 to 0,
        ).forEach { candidate ->
            val p = pos + candidate
            if (p.first in 0 until this.size && p.second in 0 until this[0].size) {
                val height = this[p.first][p.second]
                if (height in 0..(currentHeight + 1)) {
                    result.add(p)
                }
            }
        }
        return result
    }
}