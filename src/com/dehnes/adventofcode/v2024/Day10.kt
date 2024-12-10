package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day10 {
    val map = getLines().map {
        it.map { it.toString().toInt() }.toTypedArray()
    }.toTypedArray()

    @Test
    fun run() {

        val startingPositions = mutableListOf<PointInt>()
        map.indices.forEach { y ->
            map[0].indices.forEach { x ->
                if (map[y][x] == 0) {
                    startingPositions.add(PointInt(x, y))
                }
            }
        }

        val part1Trails = mutableMapOf<PointInt, MutableSet<PointInt>>()
        val part2Trails = mutableMapOf<PointInt, MutableSet<List<PointInt>>>()
        startingPositions.forEach {
            findTrail(it, listOf(it)) {
                part1Trails.getOrPut(it.first()) { hashSetOf() }.add(it.last())
                part2Trails.getOrPut(it.first()) { hashSetOf() }.add(it)
            }
        }

        check(550 == part1Trails.entries.sumOf { it.value.size })
        check(1255 == part2Trails.entries.sumOf { it.value.size })
    }

    private fun findTrail(pos: PointInt, trail: List<PointInt>, onEndReached: (p: List<PointInt>) -> Unit) {
        val currentValue = map[pos.y][pos.x]
        listOf(
            Direction.right,
            Direction.down,
            Direction.left,
            Direction.up,
        ).forEach { dir ->
            val nextPos = pos + dir
            if (nextPos.isPartOfGrid(map)) {
                val nestValue = map[nextPos.y][nextPos.x]
                if (currentValue + 1 == nestValue) {
                    if (nestValue == 9) {
                        onEndReached(trail + nextPos)
                    } else {
                        findTrail(nextPos, trail + nextPos, onEndReached)
                    }
                }
            }
        }
    }
}