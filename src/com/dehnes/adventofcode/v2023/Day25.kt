package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Combinations.combinations
import com.dehnes.adventofcode.utils.Dijkstra
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class Day25 {

    val lines = getLines().map {
        val (l, r) = it.split(": ")
        l to (r.split(" ").toSet())
    }

    val graph = mutableMapOf<String, MutableSet<String>>()

    init {
        lines.forEach { (from, tos) ->
            tos.forEach { to ->
                graph.getOrPut(from) { mutableSetOf() }.add(to)
                graph.getOrPut(to) { mutableSetOf() }.add(from)
            }
        }
    }

    @Suppress("unused")
    private fun findTop3MostUsedConnections(): List<List<String>> {
        fun getPath(from: String, to: String): List<String> {
            val distances = Dijkstra.solveWithPath(from) { c ->
                graph[c]!!.map { it to 1L }
            }
            return distances[to]!!.second
        }

        val connectionsUsed = mutableMapOf<List<String>, AtomicLong>()
        combinations(graph.keys.toList()).forEach { (l, r) ->
            val path = getPath(l, r)

            path.windowed(2, 1) { (from, to) ->
                connectionsUsed.getOrPut(listOf(from, to).sorted()) { AtomicLong(0) }.incrementAndGet()
            }
        }

        return connectionsUsed.entries.sortedByDescending { it.value.get() }.take(3).map { it.key }
    }

    @Test
    fun run() {
        // this runs for 10+ minutes
        //val top3MostUsedConnections = findTop3MostUsedConnections().toSet()

        // result:
        val top3MostUsedConnections = setOf(
            listOf("kcn", "szl"),
            listOf("fbd", "lzd"),
            listOf("fxn", "ptq"),
        )

        val updatedGraph = mutableMapOf<String, MutableSet<String>>()
        graph.entries.forEach { (from, tos) ->
            tos.forEach { to ->
                val conn = listOf(from, to).sorted()

                if (conn !in top3MostUsedConnections) {
                    updatedGraph.getOrPut(from) { mutableSetOf() }.add(to)
                    updatedGraph.getOrPut(to) { mutableSetOf() }.add(from)
                }
            }
        }

        // find group
        val group = mutableSetOf<String>()
        val todo = LinkedList<String>()
        todo.add(updatedGraph.keys.first())
        while (todo.isNotEmpty()) {
            val next = todo.poll()
            group.add(next)

            updatedGraph[next]!!.forEach { n ->
                if (n !in group) {
                    todo.add(n)
                }
            }
        }

        val g1 = group.size
        val g2 = updatedGraph.keys.size - g1
        val result = g1 * g2
        check(result == 571753)
    }
}
