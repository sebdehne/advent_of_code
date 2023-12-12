package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day12 {

    @Test
    fun run() {
        val part1 = transformLines(1).sumOf { (springs, numbers) ->
            val combinationsCached = combinationsCached(springs, numbers)
            combinationsCached
        }
        check(part1 == 7599L)

        val part2 = transformLines(5).sumOf { (springs, numbers) ->
            combinationsCached(springs, numbers)
        }
        check(part2 == 15454556629917L)
    }

    private fun transformLines(factor: Int) = getLines().map { line ->
        val (left, right) = line.split(" ")

        val newLeft = (1..factor).joinToString("?") { left }
        val newRight = (1..factor).joinToString(",") { right }

        newLeft to newRight.split(",").map { it.toInt() }
    }

    val cache = mutableMapOf<String, Long>()

    fun combinationsCached(pattern: String, remainingGroups: List<Int>, end: Boolean = false): Long =
        cache.getOrPut("$pattern=$remainingGroups=$end") {
            combinations(pattern, remainingGroups, end)
        }

    private fun combinations(
        pattern: String,
        remainingGroups: List<Int>,
        previousGroupEnded: Boolean = false
    ): Long = when {
        remainingGroups.isEmpty() && (pattern.isEmpty() || !pattern.contains("#")) -> 1
        remainingGroups.isEmpty() || (pattern.isEmpty() && remainingGroups.isNotEmpty()) -> 0

        // move one step forward if '.' or the previous group ended (and thus a '?' must be a '.')
        pattern[0] == '.' || (previousGroupEnded && pattern[0] == '?') -> combinationsCached(
            pattern.substring(1),
            remainingGroups
        )

        previousGroupEnded -> 0 // no '.' or '?' found -> fail

        else -> {
            val currentGroup = remainingGroups.first()
            var numberOfResults = 0L

            // the entire group matches, start evaluating the next group
            if (pattern.length >= currentGroup && !pattern.substring(0, currentGroup).contains(".")) {
                numberOfResults += combinationsCached(
                    pattern.substring(currentGroup),
                    remainingGroups.drop(1),
                    true
                )
            }

            // evaluate the current group against one step forward
            if (pattern[0] == '?') {
                numberOfResults += combinationsCached(pattern.substring(1), remainingGroups)
            }

            numberOfResults
        }
    }
}