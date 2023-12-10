package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.DirectionInt
import com.dehnes.adventofcode.utils.DirectionInt.Companion.all90DegreesDirections
import com.dehnes.adventofcode.utils.DirectionInt.Companion.create
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day10 {

    enum class Pipe(
        val entry1: DirectionInt,
        val entry2: DirectionInt,
    ) {
        `|`((0 to 1).create(), (0 to -1).create()),
        `-`((1 to 0).create(), (-1 to 0).create()),
        `L`((0 to 1).create(), (-1 to 0).create()),
        `J`((0 to 1).create(), (1 to 0).create()),
        `7`((0 to -1).create(), (1 to 0).create()),
        `F`((0 to -1).create(), (-1 to 0).create()),
        `S`((0 to 0).create(), (0 to 0).create()),
    }

    var startPosition: PointInt = PointInt(0, 0)
    val grid = getLines().mapIndexed { y, line ->
        line.toList().mapIndexed { x, c ->

            if (c == '.') {
                null
            } else {
                if (c == 'S') {
                    startPosition = PointInt(x, y)
                }
                Pipe.valueOf(c.toString())
            }
        }.toTypedArray()
    }.toTypedArray()
    val gridEnd = PointInt(grid[0].size, grid.size)

    private fun <T> PointInt.neighbours(fn: (dir: DirectionInt, newPos: PointInt) -> T?) = all90DegreesDirections()
        .mapNotNull { direction ->
            val p = this.moveTo(direction)
            if (!p.isPartOfGrid(grid)) {
                null
            } else {
                fn(direction, p)
            }
        }

    @Test
    fun part1() {
        val possibleStartDirections = startPosition.neighbours { dir, newPos ->
            val pipe = newPos.getFromGrid(grid)
            if (pipe == null) null else {
                if (pipe.entry1 == dir || pipe.entry2 == dir) {
                    dir
                } else null
            }
        }

        // pick a start-direction
        val startDirection = possibleStartDirections[1]

        // record the path and the direction
        val pathCurrentPosToExit = mutableListOf<Pair<PointInt, DirectionInt>>()

        var currentPos = startPosition
        var steps = 0
        var nextDirection = startDirection
        pathCurrentPosToExit.add(currentPos to nextDirection)

        while (true) {

            currentPos += nextDirection
            if (currentPos == startPosition) {
                // reached back to start
                break
            }
            val pipe = currentPos.getFromGrid(grid)!!
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
                val pos = PointInt(x, y)
                if (grid[y][x] != null && pos !in path) {
                    grid[y][x] = null
                }
            }
        }


        // group all empty tiles together into groups
        val groups = mutableListOf<MutableSet<PointInt>>()
        grid.indices.forEach { y ->
            grid[0].indices.forEach { x ->
                val pos = PointInt(x, y)
                val pipe = pos.getFromGrid(grid)

                if (pipe == null) {
                    // find all neighbours
                    val neighbours = pos.neighbours { dir, newPos ->
                        if (newPos.getFromGrid(grid) == null) newPos else null
                    }

                    // check if the neighbours already belong an existing group
                    val existingGr = groups.firstOrNull { gr ->
                        neighbours.any { n -> n in gr }
                    }

                    if (existingGr != null) {
                        existingGr.add(pos)
                    } else {
                        groups.add(mutableSetOf<PointInt>().apply { add(pos) })
                    }
                }
            }
        }


        // ignore groups along the grid edge
        val enclosedGroups = groups.filterNot { gr ->
            gr.any { tile ->
                tile.x == 0 || tile.y == 0 || tile.x == gridEnd.x || tile.y == gridEnd.y
            }
        }

        // evaluate each group to be either inside or outside, looking at the relative path direction surrounding the group
        val insideGroups = mutableListOf<MutableSet<PointInt>>()
        enclosedGroups.forEach { gr ->

            val tilesAlongPath = gr.filter { tile ->
                tile.neighbours { _, newPos -> newPos.getFromGrid(grid) }.any()
            }

            val testForEachDirection = listOf(
                listOf(
                    Pipe.`|` to true,
                    Pipe.F to true,
                    Pipe.L to false,
                ),
                listOf(
                    Pipe.`-` to true,
                    Pipe.`7` to true,
                    Pipe.F to false,
                ),
                listOf(
                    Pipe.`|` to true,
                    Pipe.`J` to true,
                    Pipe.`7` to false,
                ),
                listOf(
                    Pipe.`-` to true,
                    Pipe.`L` to true,
                    Pipe.J to false,
                ),
            )

            var isInside: Boolean? = null
            for (tile in tilesAlongPath) {

                val startDir = DirectionInt(1, 0)
                for (rot in (0..3)) {
                    val tests = testForEachDirection[rot]
                    val dir = startDir.rotate90Degrees(rot)

                    val target = pathCurrentPosToExit.firstOrNull { it.first == (tile + dir) }
                    if (target != null) {
                        val pathTile = target.first.getFromGrid(grid)!!

                        val entryDir = if (target.second.reverseDirection() == pathTile.entry1) {
                            pathTile.entry2
                        } else {
                            pathTile.entry1
                        }

                        for (test in tests) {
                            if (isInside == null && test.first == pathTile) {
                                isInside = if (test.second) {
                                    target.second
                                } else {
                                    entryDir
                                } == dir.rotate90Degrees()
                            }
                        }

                    }
                    if (isInside != null) break
                }

                if (isInside != null) break
            }

            check(isInside != null)

            if (isInside) {
                insideGroups.add(gr)
            }
        }

        val part2 = insideGroups.sumOf { it.size }
        check(part2== 417)
    }


}