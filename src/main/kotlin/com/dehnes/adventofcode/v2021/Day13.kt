package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.max

class Day13 {

    var maxX = 0
    var maxY = 0
    val map = getLines().filterNot { it.startsWith("fold") }
        .filterNot { it.isBlank() }
        .map {
            it.split(",").let {
                val x = it[0].toInt()
                val y = it[1].toInt()
                maxX = max(x, maxX)
                maxY = max(y, maxY)
                x to y
            }
        }.let {
            val map = Array(maxY + 1) { IntArray(maxX + 1) }
            it.forEach { (x, y) ->
                map[y][x] = 1
            }
            map
        }
    val foldLines = File("resources/2021/day13.txt")
        .readLines().filter { it.startsWith("fold") }.map {
            it.replace("fold along ", "").split("=").let {
                it[0] to it[1].toInt()
            }
        }

    @Test
    fun run() {
        val result = foldLines.fold(map) { acc, foldLine ->
            acc.fold(foldLine).apply { println(this.sumOf { it.count { it > 0 } }) }
        } // 710

        result.printMap() // EPLGRULR
    }

    private fun Array<IntArray>.printMap() {
        this.forEach { line ->
            println(line.joinToString(separator = "") { if (it > 0) "#" else "." })
        }
        println()
    }

    private fun merge(a: Array<IntArray>, b: Array<IntArray>): Array<IntArray> {
        val result = Array(a.size) { IntArray(a[0].size) }
        result.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, _ ->
                result[rowIndex][columnIndex] = a[rowIndex][columnIndex] + b[rowIndex][columnIndex]
            }
        }
        return result
    }

    private fun Array<IntArray>.fold(line: Pair<String, Int>) =
        if (line.first == "y") {
            splitHorizontal(line.second).let { (a, b) ->
                merge(a, b.mirror().rotate90right().rotate90right().rotate90right())
            }
        } else {
            splitVertical(line.second).let { (a, b) ->
                merge(a, b.mirror().rotate90right())
            }
        }

    private fun Array<IntArray>.splitHorizontal(lineIndex: Int): Pair<Array<IntArray>, Array<IntArray>> {
        val a = Array(lineIndex) { IntArray(this[0].size) }
        val b = Array(this.size - lineIndex - 1) { IntArray(this[0].size) }

        this.forEachIndexed { l, _ ->
            if (l < lineIndex) {
                a[l] = this[l]
            } else if (l > lineIndex) {
                b[l - lineIndex - 1] = this[l]
            }
        }

        return a to b
    }

    private fun Array<IntArray>.splitVertical(columnIndex: Int): Pair<Array<IntArray>, Array<IntArray>> {
        val a = Array(this.size) { IntArray(columnIndex) }
        val b = Array(this.size) { IntArray(this[0].size - columnIndex - 1) }

        this.forEachIndexed { l, line ->
            line.forEachIndexed { cI, _ ->
                if (cI < columnIndex) {
                    a[l][cI] = this[l][cI]
                } else if (cI > columnIndex) {
                    b[l][cI - columnIndex - 1] = this[l][cI]
                }
            }

        }

        return a to b
    }

    private fun Array<IntArray>.rotate90right(): Array<IntArray> {
        val result = Array(this[0].size) { IntArray(this.size) }
        this.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { columnIndex, column ->
                result[columnIndex][this.size - lineIndex - 1] = column
            }
        }
        return result
    }

    private fun Array<IntArray>.mirror(): Array<IntArray> {
        val result = Array(this[0].size) { IntArray(this.size) }
        this.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { columnIndex, column ->
                result[columnIndex][lineIndex] = column
            }
        }
        return result
    }

}