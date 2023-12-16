package com.dehnes.adventofcode.utils

import com.dehnes.adventofcode.utils.DirectionInt.Companion.create

typealias Point = Pair<Int, Int>

operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.minus(o: Pair<Int, Int>) = (this.first - o.first) to (this.second - o.second)

operator fun Pair<Int, Int>.times(other: Int) = this.first * other to this.second * other


data class DirectionInt(
    val x: Int,
    val y: Int,
) {
    companion object {
        fun Point.create() = DirectionInt(first, second)
        fun all90DegreesDirections(): List<DirectionInt> = listOf(
            1 to 0,
            0 to 1,
            -1 to 0,
            0 to -1
        ).map { it.create() }

        val up = DirectionInt(0, -1)
        val down = DirectionInt(0, 1)
        val right = DirectionInt(1, 0)
        val left = DirectionInt(-1, 0)
    }

    fun reverseDirection() = when (this.toPair()) {
        0 to 1 -> 0 to -1
        1 to 0 -> -1 to 0
        0 to -1 -> 0 to 1
        -1 to 0 -> 1 to 0
        else -> error("")
    }.create()

    fun rotate90Degrees(steps: Int) = (0..<steps).fold(this) { acc, i ->
        acc.rotate90Degrees()
    }

    fun toPair() = x to y

    fun rotate90Degrees() = when (this.toPair()) {
        1 to 0 -> 0 to 1
        0 to 1 -> -1 to 0
        -1 to 0 -> 0 to -1
        0 to -1 -> 1 to 0
        else -> error("")
    }.create()
}

data class PointInt(
    val x: Int,
    val y: Int,
) {

    companion object {
        fun Point.create() = PointInt(first, second)
    }

    fun <T> isPartOfGrid(grid: Array<Array<T>>) = y >= 0 && y < grid.size && x >= 0 && x < grid[0].size

    fun <T> getFromGrid(grid: Array<Array<T>>) = grid[y][x]

    fun toPoint() = x to y

    fun moveTo(dir: DirectionInt) = (toPoint() + dir.toPair()).create()

    operator fun plus(dir: DirectionInt) = moveTo(dir)
    operator fun plus(dir: Point) = (toPoint() + dir).create()
}

