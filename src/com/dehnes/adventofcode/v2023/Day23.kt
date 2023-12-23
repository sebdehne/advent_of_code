package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Direction.Companion.down
import com.dehnes.adventofcode.utils.Direction.Companion.left
import com.dehnes.adventofcode.utils.Direction.Companion.right
import com.dehnes.adventofcode.utils.Direction.Companion.up
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import java.util.*

class Day23 {

    val map = getLines().map { it.toCharArray() }.toTypedArray()
    val end = PointInt(map[0].size - 2, map.size - 1)
    val start = PointInt(1, 0)

    @Test
    fun run() {
        check(highest(buildGraph(true)) == 2306L)
        check(highest(buildGraph(false)) == 6718L)
    }

    private fun highest(graph: Map<PointInt, Map<PointInt, Int>>): Long {
        data class Path(val next: PointInt, val totalDis: Long, val path: Set<PointInt>)

        val todo = LinkedList<Path>()
        todo.add(Path(start, 0, emptySet()))
        var highest = 0L
        while (todo.isNotEmpty()) {
            val cur = todo.poll()

            graph[cur.next]!!.entries
                .filter { it.key !in cur.path }
                .forEach { (edge, dis) ->
                    val d = cur.totalDis + dis
                    if (d > highest && edge == end) {
                        highest = d
                    }
                    todo.add(Path(edge, d, cur.path + edge))
                }

        }

        return highest
    }


    private fun goToNextJunction(pos: PointInt, direction: Direction): Pair<PointInt, Int> {
        var previousDir = direction
        var currentPos = pos + direction
        var walked = 1

        while (true) {
            val next = listOf(right, down, left, up)
                .filterNot { it.reverseDirection() == previousDir }
                .mapNotNull { newDir ->
                    val newPos = currentPos + newDir
                    map.getOrNull(newPos.y)?.getOrNull(newPos.x)?.let { c ->
                        if (c == '#') null else newDir
                    }
                }

            if (next.size != 1) {
                return currentPos to walked
            } else {
                walked++
                currentPos += next.single()
                previousDir = next.single()
            }
        }
    }

    private fun buildGraph(onlyDownhill: Boolean): Map<PointInt, Map<PointInt, Int>> {

        val m = mutableMapOf<PointInt, Map<PointInt, Int>>()

        val visited = mutableSetOf<PointInt>()
        val todo = LinkedList<PointInt>()
        todo.add(start)
        while (todo.isNotEmpty()) {
            val pos = todo.poll()
            visited.add(pos)

            val connectedNext = listOf(right, down, left, up)
                .mapNotNull {
                    val newPos = pos + it
                    val c = map.getOrNull(newPos.y)?.getOrNull(newPos.x)
                    if (c == null || c == '#') {
                        null
                    } else {
                        if (c == '.' || (!onlyDownhill || when (it) {
                                right -> c == '>'
                                down -> c == 'v'
                                left -> c == '<'
                                up -> c == '^'
                                else -> error("")
                            })
                        ) it else null
                    }
                }
                .map { goToNextJunction(pos, it) }
                .associate { it.first to it.second }

            m[pos] = connectedNext

            connectedNext.forEach {
                if (it.key !in visited) {
                    todo.add(it.key)
                }
            }
        }

        return m
    }


}