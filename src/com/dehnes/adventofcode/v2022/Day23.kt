package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.v2022.Directions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day23 {

    @Test
    fun part1() {
        val width = 1000
        val height = 1000
        val offset = 500 to 500

        val grid = Array(height) { CharArray(width) { '.' } }
        val elfes = mutableListOf<Point>()

        getLines().forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    grid[y + offset.first][x + offset.second] = '#'
                    elfes.add((y + offset.first) to (x + offset.second))
                }
            }
        }

        repeat(10) { roundId ->
            val proposals = mutableMapOf<Point, MutableList<Int>>()
            elfes.forEachIndexed { index, pos ->
                if (!grid.isFree(pos)) {
                    for (d in getNextDirections(roundId.toLong())) {
                        if (grid.canMoveInto(d, pos)) {
                            val moveTo = pos + d.getStep()
                            proposals.getOrPut(moveTo) { mutableListOf() }.add(index)
                            break
                        }
                    }
                }
            }

            // perform the move
            proposals.filter { it.value.size == 1 }.forEach { (dst, e) ->
                val elfIndex = e.single()
                val oldPos = elfes[elfIndex]
                elfes[elfIndex] = dst
                grid[oldPos.first][oldPos.second] = '.'
                grid[dst.first][dst.second] = '#'
            }
        }

        val result = elfes.print()
        expectThat(result) isEqualTo 3757
    }

    @Test
    fun part2() {
        val width = 1000
        val height = 1000
        val offset = 500 to 500

        val grid = Array(height) { CharArray(width) { '.' } }
        val elfes = mutableListOf<Point>()

        getLines().forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    grid[y + offset.first][x + offset.second] = '#'
                    elfes.add((y + offset.first) to (x + offset.second))
                }
            }
        }


        var roundId = 0L
        while (true) {
            var moved = 0L

            val proposals = mutableMapOf<Point, MutableList<Int>>()
            elfes.forEachIndexed { index, pos ->
                if (!grid.isFree(pos)) {
                    for (d in getNextDirections(roundId)) {
                        if (grid.canMoveInto(d, pos)) {
                            val moveTo = pos + d.getStep()
                            proposals.getOrPut(moveTo) { mutableListOf() }.add(index)
                            break
                        }
                    }
                }
            }

            // perform the move
            proposals.filter { it.value.size == 1 }.forEach { (dst, e) ->
                val elfIndex = e.single()
                val oldPos = elfes[elfIndex]
                elfes[elfIndex] = dst
                grid[oldPos.first][oldPos.second] = '.'
                grid[dst.first][dst.second] = '#'
                moved++
            }

            if (moved == 0L) {
                break
            }
            roundId++
        }

        val result = roundId + 1
        expectThat(result) isEqualTo 918
    }


    fun List<Point>.print(): Long {
        var startY = Int.MAX_VALUE
        var startX = Int.MAX_VALUE
        var endY = Int.MIN_VALUE
        var endX = Int.MIN_VALUE
        this.forEach { elf ->
            if (elf.first < startY) {
                startY = elf.first
            }
            if (elf.first > endY) {
                endY = elf.first
            }
            if (elf.second < startX) {
                startX = elf.second
            }
            if (elf.second > endX) {
                endX = elf.second
            }
        }


        var empty = 0L
        (startY..endY).map { y ->
            val line = (startX..endX).map { x ->
                if ((y to x) in this) {
                    '#'
                } else {
                    empty++
                    '.'
                }
            }.joinToString("")
            println(line)
        }
        println("DONE")

        return empty
    }

    fun Directions.getStep() = when (this) {
        north -> -1 to 0
        south -> 1 to 0
        west -> 0 to -1
        east -> 0 to 1
    }

    fun Array<CharArray>.canMoveInto(d: Directions, pos: Point): Boolean {
        val stepsToTest = when (d) {
            north -> listOf(-1 to -1, -1 to 0, -1 to 1)
            south -> listOf(1 to -1, 1 to 0, 1 to 1)
            west -> listOf(-1 to -1, 0 to -1, 1 to -1)
            east -> listOf(-1 to 1, 0 to 1, 1 to 1)
        }

        return testAvailable(pos, stepsToTest)
    }

    fun Array<CharArray>.isFree(pos: Point) = testAvailable(
        pos,
        listOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to -1,
            0 to 1,
            1 to -1,
            1 to 0,
            1 to 1,
        )
    )

    fun Array<CharArray>.testAvailable(pos: Point, stepsToTest: List<Point>) = stepsToTest.all { s ->
        val t = pos + s
        this[t.first][t.second] == '.'
    }

    fun getNextDirections(round: Long) = when (round % 4) {
        0L -> listOf(north, south, west, east)
        1L -> listOf(south, west, east, north)
        2L -> listOf(west, east, north, south)
        3L -> listOf(east, north, south, west)
        else -> error("")
    }
}

enum class Directions {
    north,
    south,
    west,
    east;
}

