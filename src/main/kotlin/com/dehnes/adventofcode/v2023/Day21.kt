package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Direction.Companion.down
import com.dehnes.adventofcode.utils.Direction.Companion.left
import com.dehnes.adventofcode.utils.Direction.Companion.right
import com.dehnes.adventofcode.utils.Direction.Companion.up
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue


class Day21 {
    val originalMap = getLines().map { it.toList().toCharArray() }.toTypedArray()

    // find startPoint
    var originalStart: PointInt? = null

    init {
        originalMap.indices.forEach { y ->
            originalMap[0].indices.forEach { x ->
                if (originalMap[y][x] == 'S') {
                    originalStart = PointInt(x, y)
                    originalMap[y][x] = '.'
                }
            }
        }
    }

    @Test
    fun part1() {
        check(originalMap.calcPlots(originalStart!!, 64).size == 3816)
    }

    @Test
    fun part2() {
        val targetSteps = 26501365

        val data = get5x5Data()
        val a = data[1][2]
        val b = data[2][2]

        fun solve(c: Int): Long {
            val div = c / 2
            return (a * (div + 1)) + (b * div)
        }

        var totalPlots = 0L

        var growth = 0
        var delta = 1
        val expand = (targetSteps - originalStart!!.x) / originalMap.size
        val middle = (expand + 1)
        while (delta == 1 || growth >= 0) {
            val middleReached = growth + 1 == middle

            val w = growth + (growth - 1)
            totalPlots += when {
                delta == 1 && growth == 0 -> data[0][1] + data[0][2] + data[0][3]
                delta != 1 && growth == 0 -> data[4][1] + data[4][2] + data[4][3]
                middleReached -> data[2][0] + solve(w) + data[2][4]
                delta == 1 -> data[1][0] + data[1][1] + solve(w) + data[1][3] + data[1][4]
                else -> data[3][0] + data[3][1] + solve(w) + data[3][3] + data[3][4]
            }

            if (middleReached) {
                delta = -1
            }

            growth += delta
        }

        check(totalPlots == 634549784009844)
    }

    private fun Array<CharArray>.calcPlots(pos: PointInt, steps: Int): Set<PointInt> {
        val legalStepsFrom = { p: PointInt ->
            listOf(right, down, left, up).map { p + it }.mapNotNull { newPos ->
                this.getOrNull(newPos.y)?.getOrNull(newPos.x)?.let { if (it == '#') null else newPos }
            }
        }

        var plots = mutableSetOf(pos)
        var temp = mutableSetOf<PointInt>()
        repeat(steps) {
            plots.forEach { legalStepsFrom(it).forEach { temp.add(it) } }
            plots = temp
            temp = mutableSetOf()
        }

        return plots
    }

    private fun expandMap5(m: Array<CharArray>, pos: PointInt): Pair<Array<CharArray>, PointInt> {
        val factor = 5
        val result = Array(m.size * factor) { CharArray(m[0].size * factor) { '.' } }

        result.indices.forEach { y ->
            result[0].indices.forEach { x ->
                val relativePos = reduce(m, x, y)
                result[y][x] = m[relativePos.second][relativePos.first]
            }
        }

        return result to PointInt(pos.x + (m[0].size * (factor / 2)), pos.y + (m.size * (factor / 2)))
    }

    private fun get5x5Data(): Array<LongArray> {
        val (largerMap, start) = expandMap5(originalMap, originalStart!!)

        val points = largerMap.calcPlots(start, start.x)

        val width = originalMap[0].size
        val height = originalMap.size
        val sectionsToPlots = Array(5) { LongArray(5) { 0 } }
        (0..4).forEach { mapY ->
            (0..4).forEach { mapX ->
                val xRange = (width * mapX)..<((width * mapX) + width)
                val yRange = (height * mapY)..<((height * mapY) + height)

                sectionsToPlots[mapY][mapX] = points.filter { it.x in xRange && it.y in yRange }.size.toLong()
            }
        }

        return sectionsToPlots
    }

    fun reduce(m: Array<CharArray>, x: Int, y: Int): Pair<Int, Int> {
        val width = m[0].size
        val height = m.size

        val newY = if (y < 0) {
            height - 1 - ((y + 1).absoluteValue % height)
        } else if (y >= height) {
            y % height
        } else {
            y
        }

        val newX = if (x < 0) {
            width - 1 - ((x + 1).absoluteValue % width)
        } else if (x >= width) {
            x % width
        } else {
            x
        }

        return newX to newY
    }


}
