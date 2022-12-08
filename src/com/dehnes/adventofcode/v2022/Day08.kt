package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day08 {

    val lines = inputLines(8)
    val width = lines[0].length
    val grid: Array<IntArray> = Array(lines.size) { IntArray(width) { -1 } }

    init {
        lines.forEachIndexed { index1, line ->
            line.forEachIndexed { index2, c ->
                grid[index1][index2] = c - '0'
            }
        }
    }

    @Test
    fun run() {
        var part1 = 0
        var part2 = 0L

        grid.forEachIndexed { index1, line ->
            line.forEachIndexed { index2, thisTree ->
                val pos = index2 to index1
                val left = grid.getLine(pos, -1 to 0)
                val right = grid.getLine(pos, 1 to 0)
                val top = grid.getLine(pos, 0 to -1)
                val down = grid.getLine(pos, 0 to 1)

                if (left.all { it < thisTree } || right.all { it < thisTree } || top.all { it < thisTree } || down.all { it < thisTree }) {
                    part1++
                }

                val score =
                    right.countView(thisTree) * left.countView(thisTree) * top.countView(thisTree) * down.countView(
                        thisTree
                    )
                if (score > part2) {
                    part2 = score
                }
            }
        }

        expectThat(part1) isEqualTo 1823
        expectThat(part2) isEqualTo 211680
    }

    fun Array<IntArray>.getLine(start: Pair<Int, Int>, step: Pair<Int, Int>): IntArray {
        val result = mutableListOf<Int>()
        var pos = start
        try {
            while (true) {
                pos = (pos.first + step.first) to (pos.second + step.second)
                result.add(this[pos.second][pos.first])
            }
        } catch (_: Exception) {
        }

        return result.toIntArray()
    }

    fun IntArray.countView(myHeight: Int): Long {
        var result = 0L
        this.forEach { i ->
            if (i < myHeight) {
                result++
            } else {
                result++
                return result
            }
        }
        return result
    }


}