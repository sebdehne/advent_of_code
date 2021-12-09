package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day09 {

    val input = File("resources/2021/day09.txt").readLines()
        .map { it.toList().map { it.toString().toInt() }.toTypedArray() }.toTypedArray()

    @Test
    fun run() {
        val lowestPoints = mutableListOf<Pair<Int, Int>>()

        input.indices.forEach { row ->
            input[row].indices.forEach { column ->
                val value = input[row][column]
                val up = if (row > 0) input[row - 1][column] else 9
                val left = if (column > 0) input[row][column - 1] else 9
                val right = if (column < input[row].size - 1) input[row][column + 1] else 9
                val down = if (row < input.size - 1) input[row + 1][column] else 9
                if (value < up && value < left && value < right && value < down) {
                    lowestPoints.add(value to findBasin(row, column))
                }
            }
        }

        val part1 = lowestPoints.sumOf { it.first + 1 }
        val part2 = lowestPoints
            .sortedBy { it.second }
            .reversed().subList(0, 3)
            .fold(1) { acc, i -> acc * i.second }

        assertEquals(535, part1)
        assertEquals(1122700, part2)
    }

    private fun findBasin(row: Int, column: Int, alreadyVisited: MutableSet<Pair<Int, Int>> = mutableSetOf()): Int {
        if ((row to column) in alreadyVisited) {
            return 0
        }
        alreadyVisited.add((row to column))

        val value = input[row][column]
        val up = if (row > 0) input[row - 1][column] else 9
        val left = if (column > 0) input[row][column - 1] else 9
        val right = if (column < input[row].size - 1) input[row][column + 1] else 9
        val down = if (row < input.size - 1) input[row + 1][column] else 9

        var size = 1

        if (up != 9 && row > 0 && up > value) {
            size += findBasin(row - 1, column, alreadyVisited)
        }
        if (left != 9 && column > 0 && left > value) {
            size += findBasin(row, column - 1, alreadyVisited)
        }
        if (right != 9 && column < input[row].size - 1 && right > value) {
            size += findBasin(row, column + 1, alreadyVisited)
        }
        if (down != 9 && row < input.size - 1 && down > value) {
            size += findBasin(row + 1, column, alreadyVisited)
        }

        return size
    }

}