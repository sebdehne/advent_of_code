package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Direction.Companion.down
import com.dehnes.adventofcode.utils.Direction.Companion.left
import com.dehnes.adventofcode.utils.Direction.Companion.right
import com.dehnes.adventofcode.utils.Direction.Companion.up
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day12 {

    @Test
    fun run() {
        val map = getLines().map { it.toList().toTypedArray() }.toTypedArray()
        val regions = mutableListOf<Region>()
        map.indices.forEach { y ->
            map[0].indices.forEach { x ->
                val pos = PointInt(x, y)

                if (!regions.any { pos in it.plots }) {
                    Region(map[y][x]).apply {
                        addIncludingNeighbors(map, pos)
                        regions.add(this)
                    }
                }
            }
        }

        check(1467094 == regions.sumOf { it.plots.size * it.plots.entries.sumOf { it.value.size } })
        check(881182 == regions.sumOf { it.plots.size * it.countSides() })
    }

    data class Region(val value: Char, val plots: MutableMap<PointInt, MutableSet<Direction>> = mutableMapOf()) {

        fun addIncludingNeighbors(map: Array<Array<Char>>, pos: PointInt) {
            addAndOptimizeEdges(pos)

            // find and add new neighbors
            listOf(right, down, left, up).forEach { d ->
                val next = pos + d
                if (next in plots) return@forEach

                if (next.isPartOfGrid(map) && map[next.y][next.x] == value) {
                    addIncludingNeighbors(map, next)
                }
            }
        }

        private fun addAndOptimizeEdges(pos: PointInt) {
            val myEdges = mutableSetOf(right, down, left, up)
            plots[pos] = myEdges

            // remove edges between connected plots
            listOf(right, down, left, up).forEach { d ->
                val next = pos + d
                if (next !in plots) return@forEach
                val neighborEdges = plots[next]!!
                neighborEdges.remove(d.rotate90Degrees(3))
                myEdges.remove(d.rotate90Degrees())
            }
        }

        fun countSides(): Int {
            val singleSides = plots.flatMap { (pos, edges) -> edges.map { pos to it } }.toSet()
            val joinedSides = mutableSetOf<Pair<PointInt, Direction>>()
            var sideCounter = 0

            singleSides.forEach { ss ->
                if (ss in joinedSides) return@forEach
                joinedSides.add(ss)
                sideCounter++

                fun joinConnectedSidesInDirection(dir: Direction) {
                    var pos = ss.first + dir
                    while (true) {
                        if (pos to ss.second !in singleSides) break
                        joinedSides.add(pos to ss.second)
                        pos += dir
                    }
                }

                joinConnectedSidesInDirection(ss.second)
                joinConnectedSidesInDirection(ss.second.reverseDirection())
            }

            return sideCounter
        }
    }

}