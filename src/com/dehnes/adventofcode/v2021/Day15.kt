package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*

class Day15 {

    val map = getLines()
        .map { it.toList().map { it.toString().toInt() }.toIntArray() }
        .toTypedArray()

    private fun grow(map: Array<IntArray>, by: Int): Array<IntArray> {
        val fullMap = Array(map.size * by) { IntArray(map[0].size * by) }
        for (dy in 0 until by) {
            for (dx in 0 until by) {
                map.indices.forEach { y ->
                    map[y].indices.forEach { x ->
                        fullMap[y + (map.size * dy)][x + (map[0].size * dx)] =
                            (map[y][x] + dy + dx).mod(9).let { if (it == 0) 9 else it }
                    }
                }
            }
        }

        return fullMap
    }

    @Test
    fun run() {
        check(calcLowestRisk(map) == 415)
        check(calcLowestRisk(grow(map, 5)) == 2864)
        // total execution time: 240ms
    }

    private fun calcLowestRisk(map: Array<IntArray>): Int {
        val endPos = (map.size - 1) to (map[0].size - 1)
        val distances = map.map { line -> line.map { 0 }.toIntArray() }.toTypedArray()

        val q = PriorityQueue<Pair<Int, Int>> { o1, o2 ->
            distances[o1.first][o1.second] - distances[o2.first][o2.second]
        }
        q.add(0 to 0)

        while (q.isNotEmpty()) {
            val nextVertex = q.remove()

            val adjacents = mutableListOf<Pair<Int, Int>>()
            if (nextVertex.first > 0) {
                adjacents.add(nextVertex.first - 1 to nextVertex.second)
            }
            if (nextVertex.second > 0) {
                adjacents.add(nextVertex.first to nextVertex.second - 1)
            }
            if (nextVertex.first < endPos.first) {
                adjacents.add(nextVertex.first + 1 to nextVertex.second)
            }
            if (nextVertex.second < endPos.second) {
                adjacents.add(nextVertex.first to nextVertex.second + 1)
            }

            val vertexDistance = distances[nextVertex.first][nextVertex.second]
            adjacents.forEach { adjacent ->
                val newDistance = vertexDistance + map[adjacent.first][adjacent.second]
                if (distances[adjacent.first][adjacent.second] == 0 || newDistance < distances[adjacent.first][adjacent.second]) {
                    distances[adjacent.first][adjacent.second] = newDistance
                    q.add(adjacent)
                }
            }
        }
        return distances[endPos.first][endPos.second]
    }

}