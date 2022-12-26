package com.dehnes.adventofcode.utils

import org.junit.jupiter.api.Test

object Dijkstra {
    abstract class Node {
        abstract val id: String
        abstract val connections: Map<String, Long>
    }


    fun <T : Node> solve(nodes: Collection<T>, startId: String, endId: String?): MutableMap<String, Pair<Long, List<String>>> {

        val distances = nodes.associate { it.id to (Long.MAX_VALUE to emptyList<String>()) }.toMutableMap()
        distances[startId] = 0L to emptyList()

        val unexploredSet = nodes.map { it.id }.toSet().toMutableSet()

        while (unexploredSet.isNotEmpty()) {
            val currentNodeId = distances.filter { it.key in unexploredSet }.minByOrNull { it.value.first }?.key!!
            unexploredSet.remove(currentNodeId)

            if (endId != null && endId !in unexploredSet && nodes.first { it.id == endId }.connections.none { it.key in unexploredSet }) {
                return distances
            }

            val currentNode = nodes.first { it.id == currentNodeId }
            val (myDistance, path) = distances[currentNodeId]!!

            currentNode.connections
                .filter { it.key in unexploredSet }
                .forEach { (id, nDis) ->
                    val dis = myDistance + nDis

                    val pair = distances[id]!!
                    if (dis < pair.first) {
                        distances[id] = dis to (path + currentNodeId)
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
        val nodes = listOf(
            TestNode("Start", mapOf(
                "A" to 4,
                "B" to 4,
                "C" to 7
            )),
            TestNode("A", mapOf(
                "Start" to 4,
                "D" to 5
            )),
            TestNode("B", mapOf(
                "Start" to 4,
                "C" to 4,
                "D" to 6,
                "E" to 6,
                "F" to 4,
            )),
            TestNode("C", mapOf(
                "Start" to 7,
                "B" to 4,
                "F" to 3,
            )),
            TestNode("D", mapOf(
                "A" to 5,
                "B" to 6,
                "E" to 5,
                "G" to 8,
            )),
            TestNode("E", mapOf(
                "B" to 6,
                "D" to 5,
                "G" to 3,
                "F" to 2,
            )),
            TestNode("F", mapOf(
                "B" to 4,
                "C" to 3,
                "H" to 3,
                "E" to 2,
            )),
            TestNode("G", mapOf(
                "E" to 3,
                "D" to 8,
                "Goal" to 4,
            )),
            TestNode("H", mapOf(
                "F" to 3,
                "Goal" to 5,
            )),
            TestNode("Goal", mapOf(
                "H" to 5,
                "G" to 4,
            )),
        )

        val solve = Dijkstra.solve(nodes, "Start", "Goal")
        println(solve)
    }

    data class TestNode(
        override val id: String,
        override val connections: Map<String, Long>
    ) : Dijkstra.Node()
}