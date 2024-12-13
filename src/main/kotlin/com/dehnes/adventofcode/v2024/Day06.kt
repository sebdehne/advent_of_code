package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day06 {

    @Test
    fun run() {
        val map = getLines().map { it.toList().toTypedArray() }.toTypedArray()
        var start = PointInt(0, 0)
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '^') {
                    start = PointInt(x, y)
                }
            }
        }

        val path = test(start, map)

        // part1:
        check(path.map { it.first }.distinct().size == 5199)

        val obstructions = mutableSetOf<PointInt>()
        path.toList().drop(1).forEach { (pos, dir) ->
            map[pos.y][pos.x] = '#'

            try {
                test(start, map)
            } catch (e: Exception) {
                obstructions.add(pos)
                return@forEach
            } finally {
                map[pos.y][pos.x] = '.'
            }
        }

        // part2:
        check(obstructions.size == 1915)
    }

    private fun test(start: PointInt, map: Array<Array<Char>>): LinkedHashSet<Pair<PointInt, Direction>> {
        var pos = start
        var direction = Direction.up
        val path = LinkedHashSet<Pair<PointInt, Direction>>()
        while (true) {
            val next = pos + direction
            if (!next.isPartOfGrid(map)) {
                break
            }
            if (next.getFromGrid(map) == '#') {
                direction = direction.rotate90Degrees()
                continue
            }
            pos = next

            check(path.add(pos to direction)) {
                error("LOOP")
            }
        }

        return path
    }

}