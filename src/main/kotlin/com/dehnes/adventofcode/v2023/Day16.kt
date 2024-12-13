package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Direction.Companion.down
import com.dehnes.adventofcode.utils.Direction.Companion.left
import com.dehnes.adventofcode.utils.Direction.Companion.right
import com.dehnes.adventofcode.utils.Direction.Companion.up
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day16 {
    val map = getLines().map { it.toCharArray() }.toTypedArray()

    @Test
    fun part1() {
        check(calc(PointInt(0, 0), right) == 7870)
    }

    @Test
    fun part2() {
        val startPositions = mutableListOf<Pair<PointInt, Direction>>()
        val yMax = map.size - 1
        val xMax = map[0].size - 1
        map.indices.forEach { y ->
            map[0].indices.forEach { x ->
                val pos = PointInt(x, y)
                if (y == 0) {
                    startPositions.add(pos to down)
                    if (x == 0) {
                        startPositions.add(pos to right)
                    } else if (x == xMax) {
                        startPositions.add(pos to left)
                    }
                } else if (y == yMax) {
                    startPositions.add(pos to up)
                    if (x == 0) {
                        startPositions.add(pos to right)
                    } else if (x == xMax) {
                        startPositions.add(pos to left)
                    }
                } else {
                    if (x == 0) {
                        startPositions.add(pos to right)
                    } else if (x == xMax) {
                        startPositions.add(pos to left)
                    }
                }
            }
        }

        var max = 0
        startPositions.forEach { (pos, dir) ->
            val c = calc(pos, dir)
            if (c > max) {
                max = c
            }
        }

        check(max == 8143)
    }

    fun calc(p: PointInt, dir: Direction): Int {
        val energized = mutableSetOf<Pair<PointInt, Direction>>()
        followBeam(p, dir, energized)
        return energized.distinctBy { it.first }.count()
    }

    fun followBeam(p: PointInt, beam: Direction, energized: MutableSet<Pair<PointInt, Direction>>) {
        var dir = beam
        var pos = p
        while (true) {
            val nextChar = map.getOrNull(pos.y)?.getOrNull(pos.x) ?: return
            if (!energized.add(pos to dir)) return

            val (nextDirection, alternativDirection) = when (nextChar) {
                '.' -> dir to null
                '-' -> {
                    if (dir.y == 0) {
                        dir to null
                    } else {
                        right to left
                    }
                }

                '|' -> {
                    if (dir.x == 0) {
                        dir to null
                    } else {
                        up to down
                    }
                }

                '/' -> {
                    when (dir) {
                        up -> right
                        left -> down
                        right -> up
                        down -> left
                        else -> error("")
                    } to null
                }

                '\\' -> {
                    when (dir) {
                        up -> left
                        left -> up
                        right -> down
                        down -> right
                        else -> error("")
                    } to null
                }

                else -> error("")
            }

            if (alternativDirection != null) {
                followBeam(pos, alternativDirection, energized)
            }

            dir = nextDirection
            pos += dir
        }
    }

}