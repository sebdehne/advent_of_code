package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day11 {

    val map = getLines().map { it.toList() }
    val emptyRows = mutableListOf<Int>()
    val emptyColumns = mutableListOf<Int>()
    val galaxies = mutableListOf<PointInt>()
    val pairs = mutableSetOf<Pair<PointInt, PointInt>>()

    init {
        map.forEachIndexed { y, line ->
            if (line.all { it == '.' }) {
                emptyRows.add(y)
            }
        }
        map[0].indices.forEach { x ->
            val column = map.indices.map { y ->
                map[y][x]
            }

            if (column.all { it == '.' }) {
                emptyColumns.add(x)
            }
        }

        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    galaxies.add(PointInt(x, y))
                }
            }
        }

        galaxies.forEach { left ->
            galaxies.forEach { right ->
                if (left != right) {
                    val p1 = left to right
                    val p2 = right to left

                    if (p1 !in pairs && p2 !in pairs) (pairs.add(p1))
                }
            }
        }
    }

    @Test
    fun run() {
        check(calcDistances(2) == 10490062L)
        check(calcDistances(1000000) == 382979724122L)
    }

    private fun calcDistances(expansionFactor: Int): Long = pairs.map { (left, right) ->

        val xRange = if (left.x < right.x) {
            left.x..right.x
        } else {
            right.x..left.x
        }
        var dx = xRange.last - xRange.first

        emptyColumns.forEach { ec ->
            if (ec in xRange) {
                dx += (expansionFactor - 1)
            }
        }

        val yRange = if (left.y < right.y) {
            left.y..right.y
        } else {
            right.y..left.y
        }
        var dy = yRange.last - yRange.first

        emptyRows.forEach { ec ->
            if (ec in yRange) {
                dy += (expansionFactor - 1)
            }
        }

        (dx + dy).toLong()
    }.sum()

}