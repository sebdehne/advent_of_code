package com.dehnes.adventofcode.utils

import org.junit.jupiter.api.Test
import java.util.*

object Dijkstra {

    fun <T> solve(start: T, edges: (n: T) -> List<Pair<T, Long>>): Map<T, Long> {
        val distances = mutableMapOf<T, Long>()
        distances[start] = 0L
        val todo = LinkedList<Pair<Long, T>>()
        todo.offer(0L to start)
        while (todo.isNotEmpty()) {
            val (d, n) = todo.poll()
            if (d > distances[n]!!) continue
            edges(n).forEach { (edge, weight) ->
                val distance = weight + d
                if (distance < (distances[edge] ?: Long.MAX_VALUE)) {
                    distances[edge] = distance
                    todo.offer(distance to edge)
                }
            }
        }

        return distances
    }
}

class DijkstraTest {
    @Test
    fun test() {
        // https://www.baeldung.com/cs/dijkstra-vs-a-pathfinding
        val nodes = mapOf(
            "Start" to mapOf(
                "A" to 4,
                "B" to 4,
                "C" to 7
            ),
            "A" to mapOf(
                "Start" to 4,
                "D" to 5
            ),
            "B" to mapOf(
                "Start" to 4,
                "C" to 4,
                "D" to 6,
                "E" to 6,
                "F" to 4,
            ),
            "C" to mapOf(
                "Start" to 7,
                "B" to 4,
                "F" to 3,
            ),
            "D" to mapOf(
                "A" to 5,
                "B" to 6,
                "E" to 5,
                "G" to 8,
            ),
            "E" to mapOf(
                "B" to 6,
                "D" to 5,
                "G" to 3,
                "F" to 2,
            ),
            "F" to mapOf(
                "B" to 4,
                "C" to 3,
                "H" to 3,
                "E" to 2,
            ),
            "G" to mapOf(
                "E" to 3,
                "D" to 8,
                "Goal" to 4,
            ),
            "H" to mapOf(
                "F" to 3,
                "Goal" to 5,
            ),
            "Goal" to mapOf(
                "H" to 5,
                "G" to 4,
            ),
        )

        val solve = Dijkstra.solve("Start") { n: String ->
            nodes[n]!!.entries.map {
                it.key to (it.value.toLong())
            }
        }

        println(solve)
    }
}