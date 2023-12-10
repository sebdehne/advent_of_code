package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.v2022.Point
import com.dehnes.adventofcode.v2022.plus
import org.junit.jupiter.api.Test

class Day10 {

    enum class Pipe(
        val entry1: Point,
        val entry2: Point,
    ) {
        `|`(0 to 1, 0 to -1),
        `-`(1 to 0, -1 to 0),
        `L`(0 to 1, -1 to 0),
        `J`(0 to 1, 1 to 0),
        `7`(0 to -1, 1 to 0),
        `F`(0 to -1, -1 to 0),
        `S`(0 to 0, 0 to 0),
    }

    var sLoc: Point = 0 to 0
    val grid = getLines().mapIndexed { y, line ->
        line.toList().mapIndexed { x, c ->

            if (c == '.') {
                null
            } else {
                if (c == 'S') {
                    sLoc = x to y
                }
                Pipe.valueOf(c.toString())
            }
        }.toTypedArray()
    }.toTypedArray()
    val gridEnd = grid[0].size to grid.size


    @Test
    fun part1() {

        fun <T> neighbours(pos: Point, fn: (dir: Point, newPos: Point) -> T?) = listOf(
            1 to 0,
            0 to 1,
            -1 to 0,
            0 to -1
        ).mapNotNull { direction ->
            val p = pos + direction
            if (p.first < 0 || p.second < 0 || p.first >= gridEnd.first || p.second >= gridEnd.second) {
                null
            } else {
                fn(direction, p)
            }
        }

        val startDirections = neighbours(sLoc) { dir, newPos ->
            val pipe = grid[newPos.second][newPos.first]
            if (pipe == null) null else {
                if (pipe.entry1 == dir || pipe.entry2 == dir) {
                    dir
                } else null
            }
        }

        // pick a start-direction
        val startDirection = startDirections[1]

        val pathCurrentPosToExit = mutableListOf<Pair<Point, Point>>()

        var currentPos = sLoc
        var steps = 0
        var nextDirection = startDirection

        pathCurrentPosToExit.add(currentPos to nextDirection)

        while (true) {

            currentPos += nextDirection
            if (currentPos == sLoc) {
                // reached back to start
                break
            }
            val pipe = grid[currentPos.second][currentPos.first]!!
            steps++

            nextDirection = if (pipe.entry1 == nextDirection) {
                pipe.entry2.reverseDirection()
            } else {
                pipe.entry1.reverseDirection()
            }

            pathCurrentPosToExit.add(currentPos to nextDirection)
        }

        val part1 = (steps + 1) / 2
        check(part1 == 7005)

        // clean the grid
        val path = pathCurrentPosToExit.map { it.first }.toSet()
        grid.indices.forEach { y ->
            grid[0].indices.forEach { x ->
                val pos = x to y
                if (grid[y][x] != null && pos !in path) {
                    grid[y][x] = null
                }
            }
        }


        // group all empty tiles together
        val groups = mutableListOf<MutableSet<Point>>()
        grid.indices.forEach { y ->
            grid[0].indices.forEach { x ->
                val pos = x to y
                val pipe = grid[y][x]

                if (pipe == null) {
                    // find all neighbours
                    val neighbours = neighbours(pos) { dir, newPos ->
                        if (grid[newPos.second][newPos.first] == null) newPos else null
                    }

                    // check if the neighbours already belong an existing group
                    val existingGr = groups.firstOrNull { gr ->
                        neighbours.any { n -> n in gr }
                    }

                    if (existingGr != null) {
                        existingGr.add(pos)
                    } else {
                        groups.add(mutableSetOf<Point>().apply { add(pos) })
                    }
                }
            }
        }


        // filter groups which are fully enclosed
        val enclosedGroups = groups.filterNot { gr ->
            gr.any { tile ->
                tile.first == 0 || tile.second == 0 || tile.first == gridEnd.first || tile.second == gridEnd.second
            }
        }

        val insideGroups = mutableListOf<MutableSet<Point>>()

        // inside or outside
        enclosedGroups.forEach { gr ->

            val outerTilesOfGroup = gr.filter { tile ->
                neighbours(tile) { _, newPos -> grid[newPos.second][newPos.first] }.any()
            }

            var isInside: Boolean? = null
            for (tile in outerTilesOfGroup) {

                val right = pathCurrentPosToExit.firstOrNull { it.first == (tile + (1 to 0)) }
                if (right != null) {
                    val pathTile = grid[right.first.second][right.first.first]!!

                    val entryDir = if (right.second.reverseDirection() == pathTile.entry1) {
                        pathTile.entry2
                    } else {
                        pathTile.entry1
                    }

                    if (pathTile == Pipe.`|`) {
                        isInside = right.second == 0 to 1
                        break
                    }
                    if (pathTile == Pipe.F) {
                        isInside = right.second == 0 to 1
                        break
                    }
                    if (pathTile == Pipe.L) {
                        isInside = entryDir == 0 to 1
                        break
                    }
                }


                val left = pathCurrentPosToExit.firstOrNull { it.first == (tile + (-1 to 0)) }
                if (left != null) {
                    val pathTile = grid[left.first.second][left.first.first]!!
                    val entryDir = if (left.second.reverseDirection() == pathTile.entry1) {
                        pathTile.entry2
                    } else {
                        pathTile.entry1
                    }

                    if (pathTile == Pipe.`|`) {
                        isInside = left.second == 0 to -1
                        break
                    }
                    if (pathTile == Pipe.`J`) {
                        isInside = left.second == 0 to -1
                        break
                    }
                    if (pathTile == Pipe.`7`) {
                        isInside = entryDir == 0 to -1
                        break
                    }
                }

                val up = pathCurrentPosToExit.firstOrNull { it.first == (tile + (0 to -1)) }
                if (up != null) {
                    val pathTile = grid[up.first.second][up.first.first]!!
                    val entryDir = if (up.second.reverseDirection() == pathTile.entry1) {
                        pathTile.entry2
                    } else {
                        pathTile.entry1
                    }

                    if (pathTile == Pipe.`-`) {
                        isInside = up.second == 1 to 0
                        break
                    }
                    if (pathTile == Pipe.`L`) {
                        isInside = up.second == 1 to 0
                        break
                    }
                    if (pathTile == Pipe.J) {
                        isInside = entryDir == 1 to 0
                        break
                    }
                }

                val down = pathCurrentPosToExit.firstOrNull { it.first == (tile + (0 to 1)) }
                if (down != null) {
                    val pathTile = grid[down.first.second][down.first.first]!!
                    val entryDir = if (down.second.reverseDirection() == pathTile.entry1) {
                        pathTile.entry2
                    } else {
                        pathTile.entry1
                    }
                    if (pathTile == Pipe.`-`) {
                        isInside = down.second == -1 to 0
                        break
                    }
                    if (pathTile == Pipe.`7`) {
                        isInside = down.second == -1 to 0
                        break
                    }
                    if (pathTile == Pipe.F) {
                        isInside = entryDir == -1 to 0
                        break
                    }
                }

            }

            check(isInside != null)

            if (isInside) {
                insideGroups.add(gr)
            }
        }

        check(insideGroups.sumOf { it.size } == 417)
    }

    private fun Point.reverseDirection() = when (this) {
        0 to 1 -> 0 to -1
        1 to 0 -> -1 to 0
        0 to -1 -> 0 to 1
        -1 to 0 -> 1 to 0
        else -> error("")
    }

}