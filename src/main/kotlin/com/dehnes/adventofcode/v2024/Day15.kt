package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Direction.Companion.fromChar
import com.dehnes.adventofcode.utils.Maps.findPos
import com.dehnes.adventofcode.utils.Maps.forEachPos
import com.dehnes.adventofcode.utils.Maps.get
import com.dehnes.adventofcode.utils.Maps.set
import com.dehnes.adventofcode.utils.ParserUtils.getText
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day15 {

    @Test
    fun run() {
        check(1406392 == solveMap(false).getGpsSum())
        check(1429013 == solveMap(true).getGpsSum())
    }

    private fun solveMap(doubleWide: Boolean): Array<Array<Char>> {
        val (map, directions, start) = parseMapAndDirectionsAndStart(doubleWide)

        var pos = start
        directions.forEach { dir ->
            var toBeMoved = mutableListOf<PointInt>()
            if (map.canMove(pos, dir, toBeMoved)) {
                when (dir) {
                    Direction.up -> toBeMoved.sortedBy { it.y }
                    Direction.down -> toBeMoved.sortedByDescending { it.y }
                    Direction.right -> toBeMoved.sortedByDescending { it.x }
                    Direction.left -> toBeMoved.sortedBy { it.x }
                    else -> error("Unexpected direction: $dir")
                }.forEach { p ->
                    map[p + dir] = map.get(p)
                    map[p] = '.'
                }

                pos = pos + dir
            }
        }

        return map
    }

    private fun parseMapAndDirectionsAndStart(doubleWide: Boolean): Triple<Array<Array<Char>>, List<Direction>, PointInt> {
        val (mapStr, directionsStr) = getText().split("\n\n")

        val map = mapStr.lines().map {
            it.trim().toList().flatMap { c ->
                if (doubleWide) {
                    when (c) {
                        '#' -> listOf('#', '#')
                        'O' -> listOf('[', ']')
                        '.' -> listOf('.', '.')
                        '@' -> listOf('@', '.')
                        else -> error("")
                    }
                } else {
                    listOf(c)
                }
            }.toTypedArray()
        }.toTypedArray()

        return Triple(
            map,
            directionsStr.replace("\n", "").toList().map { it.fromChar() },
            map.findPos('@')!!
        )
    }

    private fun Array<Array<Char>>.canMove(pos: PointInt, dir: Direction, result: MutableList<PointInt>): Boolean {
        if (pos in result) return true
        val c = this.get(pos)
        return when (c) {
            'O', '@' -> {
                result.add(pos)
                canMove(pos + dir, dir, result)
            }

            '#' -> false
            '.' -> true
            '[' -> when (dir) {
                Direction.left, Direction.right -> {
                    result.add(pos)
                    result.add(pos + dir)
                    canMove(pos + dir + dir, dir, result)
                }

                else -> {
                    result.add(pos)
                    result.add(pos + Direction.right)
                    val nextPos = pos + dir
                    if (this.get(nextPos) == '[') {
                        canMove(pos + dir, dir, result)
                    } else {
                        canMove(pos + dir, dir, result) && canMove(pos + Direction.right + dir, dir, result)
                    }
                }
            }

            ']' -> when (dir) {
                Direction.left, Direction.right -> {
                    result.add(pos)
                    result.add(pos + dir)
                    canMove(pos + dir + dir, dir, result)
                }

                else -> {
                    result.add(pos + Direction.left)
                    result.add(pos)
                    val nextPos = pos + dir
                    if (this.get(nextPos) == ']') {
                        canMove(pos + dir, dir, result)
                    } else {
                        canMove(pos + Direction.left + dir, dir, result) && canMove(pos + dir, dir, result)
                    }
                }
            }

            else -> error("")
        }
    }

    private fun Array<Array<Char>>.getGpsSum(): Int {
        var result = 0
        forEachPos { pos, value ->
            if (value == '[' || value == 'O') {
                result += pos.y * 100 + pos.x
            }
        }
        return result
    }

}