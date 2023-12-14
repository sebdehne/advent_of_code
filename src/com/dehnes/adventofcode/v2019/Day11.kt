package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day11 {

    val originalCode = getText().split(",").map { it.toLong() }

    @Test
    fun run() {

        var pos = 0 to 0
        var direction = 0
        val grid = mutableMapOf<Pair<Int, Int>, Int>()
        grid[pos] = 1

        var outputColor = -1
        intcodeComputer(
            originalCode,
            {
                grid[pos]?.toLong() ?: 0
            },
            {
                if (outputColor == -1) {
                    outputColor = it.toInt()
                } else {
                    grid[pos] = outputColor
                    outputColor = -1
                    direction = if (it == 0L) {
                        (direction + 3).mod(4)
                    } else {
                        (direction + 1).mod(4)
                    }
                    pos = when (direction) {
                        0 -> -1 to 0
                        1 -> 0 to 1
                        2 -> 1 to 0
                        3 -> 0 to -1
                        else -> error("")
                    }.let { delta -> (pos.first + delta.first) to (pos.second + delta.second) }
                }
            }
        )
        val lowY = grid.map { it.key.first }.minOf { it }
        val highY = grid.map { it.key.first }.maxOf { it }
        val lowX = grid.map { it.key.second }.minOf { it }
        val highX = grid.map { it.key.second }.maxOf { it }

        // FKEKCFRK
        for (y in lowY..highY) {
            println(
                (lowX..highX).joinToString("") { x ->
                    if (grid[y to x] == 1) "#" else " "
                }
            )
        }
    }

}