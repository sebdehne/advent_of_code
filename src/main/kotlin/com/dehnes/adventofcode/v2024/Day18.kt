package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Dijkstra
import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Maps.get
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import java.util.*

class Day18 {

    @Test
    fun run2() {
        val size = 71
        val part1Steps = 1024

        val start = PointInt(0, 0)
        val end = PointInt(size - 1, size - 1)
        val grid = Array(size) { Array(size) { '.' } }
        val bytes = LinkedList(
            getLines().map {
                it.split(",").let {
                    PointInt(it[0].toInt(), it[1].toInt())
                }
            }
        )

        fun solve() = Dijkstra.solve(start) { pos ->
            listOf(
                Direction.right,
                Direction.down,
                Direction.left,
                Direction.up,
            ).mapNotNull {
                val next = pos + it
                if (next.isPartOfGrid(grid) && grid.get(next) == '.') {
                    next to 1L
                } else null
            }
        }[end]

        var part1 = 0L
        var part2 = start
        var steps = 0
        while (true) {
            steps++

            val b = bytes.removeFirst()
            grid[b.y][b.x] = '#'

            if (steps == part1Steps) {
                part1 = solve()!!
            } else if (steps > part1Steps) {
                if (solve() == null) {
                    part2 = b
                    break
                }
            }
        }


        check(part1 == 324L)
        check(part2 == PointInt(46, 23))
    }

}