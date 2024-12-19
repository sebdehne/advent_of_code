package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day19 {

    @Test
    fun run() {
        val (f, s) = getText().split("\n\n")
        val patterns = f.split(", ").toList()

        fun String.countCombinations(isPart1: Boolean): Long {
            val cache = mutableMapOf<String, Long>()

            fun countCombinationsCached(str: String, candidates: List<String>): Long =
                cache.getOrPut("$str-$candidates") {
                    candidates.sumOf { p ->
                        val remaining = str.drop(p.length).ifEmpty {
                            check(!isPart1) // bail out
                            return@sumOf 1
                        }
                        countCombinationsCached(
                            remaining,
                            patterns.filter { remaining.startsWith(it) }.ifEmpty { return@sumOf 0 },
                        )
                    }
                }

            return try {
                countCombinationsCached(this, patterns.filter { this.startsWith(it) })
            } catch (_: Exception) {
                return 1L
            }
        }

        check(s.lines().sumOf { it.countCombinations(true) } == 258L)
        check(s.lines().sumOf { it.countCombinations(false) } == 632423618484345L)
    }
}