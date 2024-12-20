package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Dijkstra
import com.dehnes.adventofcode.utils.Direction.Companion.down
import com.dehnes.adventofcode.utils.Direction.Companion.left
import com.dehnes.adventofcode.utils.Direction.Companion.right
import com.dehnes.adventofcode.utils.Direction.Companion.up
import com.dehnes.adventofcode.utils.Maps.forEachPos
import com.dehnes.adventofcode.utils.Maps.get
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day20 {

    @Test
    fun run2() {
        val map = getLines().map { it.toList().toTypedArray() }.toTypedArray()
        var start = PointInt(0, 0)
        map.forEachPos { pos, v ->
            if (v == 'S') {
                start = pos
            } else if (v == 'E') {
                map[pos.y][pos.x] = '.'
            }
        }

        val pathWithoutCheats = Dijkstra.solve(start) { pos ->
            listOf(right, down, left, up).mapNotNull { dir ->
                val nextPos = pos + dir
                if (map.get(nextPos) == '.') {
                    nextPos to 1
                } else null
            }
        }.toList().sortedBy { it.second }

        fun solveForCheatAllowance(cheatAllowance: Int): Long {
            var count = 0L
            pathWithoutCheats.forEachIndexed { i, (pos, dist) ->
                (pathWithoutCheats.size - 1 downTo i + 1).forEach { j ->
                    val (pos2, dist2) = pathWithoutCheats[j]

                    val manhattanDistance = (pos.y - pos2.y).absoluteValue + (pos.x - pos2.x).absoluteValue
                    if (manhattanDistance <= cheatAllowance && dist2 - dist - manhattanDistance >= 100) {
                        count++
                    }
                }
            }
            return count
        }

        check(1497L == solveForCheatAllowance(2))
        check(1030809L == solveForCheatAllowance(20))
    }
}
