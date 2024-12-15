package com.dehnes.adventofcode.utils

typealias Point = Pair<Int, Int>

operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.minus(o: Pair<Int, Int>) = (this.first - o.first) to (this.second - o.second)
operator fun Pair<Int, Int>.times(other: Int) = this.first * other to this.second * other


data class Direction(
    val x: Int,
    val y: Int,
) {
    companion object {
        fun Char.fromChar(): Direction = when(this) {
            '^' -> Direction.up
            '>' -> Direction.right
            'v' -> Direction.down
            '<' -> Direction.left
            else -> error("Invalid Direction $this")
        }

        fun Direction.toChar() = when (this) {
            Direction.up -> '^'
            Direction.down -> 'v'
            Direction.right -> '>'
            Direction.left -> '<'
            else ->  error("")
        }
        fun Point.create() = Direction(first, second)
        fun all90DegreesDirections(): List<Direction> = listOf(
            1 to 0,
            0 to 1,
            -1 to 0,
            0 to -1
        ).map { it.create() }

        val up = Direction(0, -1)
        val down = Direction(0, 1)
        val right = Direction(1, 0)
        val left = Direction(-1, 0)
    }

    fun reverseDirection(): Direction = Direction(x * -1, y * -1)

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
        fun create(p: Point) = PointInt(p.first, p.second)
    }

    fun <T> isPartOfGrid(grid: Array<Array<T>>) = y >= 0 && y < grid.size && x >= 0 && x < grid[0].size

    fun <T> getFromGrid(grid: Array<Array<T>>) = grid[y][x]

    fun toPoint() = x to y

    fun moveTo(dir: Direction) = create(toPoint() + dir.toPair())

    operator fun plus(dir: Direction) = moveTo(dir)
    operator fun plus(dir: Point) = create(toPoint() + dir)
    operator fun plus(delta: PointInt) = PointInt(x + delta.x, y + delta.y)
    operator fun minus(delta: PointInt) = PointInt(x - delta.x, y - delta.y)
}

