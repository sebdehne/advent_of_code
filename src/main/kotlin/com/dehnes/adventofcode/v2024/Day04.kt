package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day04 {

    @Test
    fun run() {
        val grid = getLines().map { it.toList().toTypedArray() }.toTypedArray()

        var part1 = 0L
        grid.indices.forEach { y ->
            grid[0].indices.forEach { x ->
                part1 += listOf(
                    Direction(1, 0),
                    Direction(1, 1),
                    Direction(0, 1),
                    Direction(-1, 1),
                    Direction(-1, 0),
                    Direction(-1, -1),
                    Direction(0, -1),
                    Direction(1, -1),
                ).map { grid.getWord(PointInt(y, x), it, 4) }.count { it == "XMAS" }
            }
        }
        check(part1 == 2554L)

        var part2 = 0L
        grid.indices.forEach { y ->
            grid[0].indices.forEach { x ->
                val pos = PointInt(y, x)

                if (pos.getFromGrid(grid) == 'A' && listOf(
                        Direction(1, 1),
                        Direction(1, -1),
                    ).all {
                        listOf(it, it.reverseDirection())
                            .joinToString("") { grid.getWord(pos, it, 2) } in listOf("ASAM", "AMAS")
                    }
                ) {
                    part2++
                }
            }
        }
        check(part2 == 1916L)
    }

    private fun Array<Array<Char>>.getWord(pos: PointInt, d: Direction, len: Int): String {
        var p = pos
        var result = ""
        repeat(len) {
            if (!p.isPartOfGrid(this)) return result
            result += p.getFromGrid(this)
            p = p.moveTo(d)
        }

        return result
    }

}