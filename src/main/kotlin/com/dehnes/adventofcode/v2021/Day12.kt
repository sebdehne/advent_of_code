package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day12 {

    @Test
    fun run() {

        val map: MutableMap<String, MutableSet<String>> = mutableMapOf()
        getLines().forEach { connection ->
            val (from, to) = connection.split("-").let { it[0] to it[1] }
            map.getOrPut(from) { mutableSetOf() }.add(to)
            map.getOrPut(to) { mutableSetOf() }.add(from)
        }

        val validPathsPart1 = mutableListOf<List<String>>().apply {
            findPath(listOf("start"), map, this, 1)
        }

        val validPathsPart2 = mutableListOf<List<String>>().apply {
            findPath(listOf("start"), map, this, 2)
        }

        check(validPathsPart1.size == 4549)
        check(validPathsPart2.size == 120535)
    }

    private fun findPath(
        currentPath: List<String>,
        map: Map<String, Set<String>>,
        validPaths: MutableList<List<String>>,
        smallAllowedTimes: Int
    ) {
        val candidates = map[currentPath.last()]!!.filter { candidate ->
            when {
                candidate == "start" -> false
                candidate == "end" -> {
                    validPaths.add(currentPath + candidate)
                    false
                }

                candidate.first().isUpperCase() -> true
                else -> {
                    val nodeOccurrences = currentPath.groupBy { it }.map { it.key to it.value.size }.toMap()
                    val smallCaveVisitedTwice =
                        nodeOccurrences.toList().firstOrNull { it.first.first().isLowerCase() && it.second > 1 }

                    if (smallCaveVisitedTwice == null || smallCaveVisitedTwice.first == candidate) {
                        (nodeOccurrences[candidate] ?: 0) < smallAllowedTimes
                    } else {
                        candidate !in nodeOccurrences
                    }
                }
            }
        }

        candidates.forEach { findPath(currentPath + it, map, validPaths, smallAllowedTimes) }
    }
}