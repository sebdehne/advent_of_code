package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day08 {
    private val grid = getLines().map { it.toList().toTypedArray() }.toTypedArray()
    private val antennas = mutableSetOf<Antenna>()

    init {
        grid.indices.forEach { y ->
            grid[0].indices.forEach { x ->
                val c = grid[y][x]
                if (c != '.') {
                    antennas.add(Antenna(PointInt(x, y), c))
                }
            }
        }
    }

    @Test
    fun run() {
        check(276 == solve(part2 = false))
        check(991 == solve(part2 = true))
    }

    private fun solve(part2: Boolean): Int {
        val antinodeLocations = mutableSetOf<PointInt>()
        antennas.forEach { a ->
            antennas.filter { it.frequency == a.frequency && it != a }.forEach { b ->
                val delta = PointInt(b.pos.x - a.pos.x, b.pos.y - a.pos.y)

                listOf(
                    ::plus to b.pos,
                    ::minus to a.pos,
                ).forEach { (operator, start) ->
                    var pos = start
                    while (true) {
                        pos = operator(pos, delta)
                        if (pos.isPartOfGrid(grid)) {
                            antinodeLocations.add(pos)
                        } else {
                            break
                        }
                        if (!part2) break
                    }
                    if (part2) antinodeLocations.add(start)
                }
            }
        }
        return antinodeLocations.size
    }

    data class Antenna(val pos: PointInt, val frequency: Char)

    private fun plus(p: PointInt, delta: PointInt) = PointInt(p.x + delta.x, p.y + delta.y)
    private fun minus(p: PointInt, delta: PointInt) = PointInt(p.x - delta.x, p.y - delta.y)
}