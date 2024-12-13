package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day11 {

    @Test
    fun run() {
        val input = getLines()
            .map { it.toList().map { it.toString().toInt() }.toTypedArray() }
            .toTypedArray()

        var flashCounter100Steps: Long = 0
        val synchronousFlashStep: Int

        var step = 1
        while (true) {
            input.indices.forEach { row ->
                input[row].indices.forEach { column ->
                    input[row][column]++
                }
            }

            val flashed = flash(input).apply {
                if (step <= 100) {
                    flashCounter100Steps += this.size
                }
            }

            if (flashed.size == 100) {
                synchronousFlashStep = step
                break
            }

            flashed.forEach { input[it.first][it.second] = 0 }
            step++
        }

        check(flashCounter100Steps == 1608L)
        check(synchronousFlashStep == 214)
    }

    private fun flash(input: Array<Array<Int>>): Set<Pair<Int, Int>> {
        val flashed = mutableSetOf<Pair<Int, Int>>()

        var flashedSome = true
        while (flashedSome) {
            flashedSome = false

            input.indices.forEach { row ->
                input[row].indices.forEach { column ->
                    if (input[row][column] > 9 && (row to column) !in flashed) {
                        flashed.add((row to column))
                        input.increaseAdjacent(row, column)
                        flashedSome = true
                    }
                }
            }
        }
        return flashed
    }


    private fun Array<Array<Int>>.increaseAdjacent(row: Int, column: Int) {
        ((row - 1)..(row + 1)).forEach { r ->
            ((column - 1)..(column + 1)).forEach { c ->
                if (r != row || c != column) {
                    try {
                        this[r][c]++
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
        }
    }

}