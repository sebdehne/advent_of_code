package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Combinations.combinations
import com.dehnes.adventofcode.utils.ParserUtils.getText
import com.dehnes.adventofcode.utils.runParallel
import org.junit.jupiter.api.Test

class Day05 {

    @Test
    fun run() {
        val (rulesStr, updatesStr) = getText().split("\n\n")
        val rules = rulesStr.lines().map {
            val (l, r) = it.split("|").map { it.toInt() }
            l to r
        }
        val updates = updatesStr.lines().map { it.split(",").map { it.toInt() } }

        val (alreadyInOrder, wrongOrder) = updates.partition { update ->
            combinations(update).all { (l, r) ->
                rules.any { it.first == l && it.second == r }
            }
        }

        val part1 = alreadyInOrder.sumOf { u -> u[u.size / 2] }
        check(part1 == 5391)

        fun fixUpdate(updateIn: List<Int>): List<Int> {
            val update = updateIn.toMutableList()
            fun swap(sa: Int, sb: Int) {
                val ia = update.indexOf(sa)
                val ib = update.indexOf(sb)
                val tmp = update[ia]
                update[ia] = update[ib]
                update[ib] = tmp
            }

            while (true) {
                if (combinations(update).all { (l, r) ->
                        rules.any { it.first == l && it.second == r }.apply {
                            if (!this) {
                                swap(l, r)
                            }
                        }
                    }) break
            }

            return update
        }

        val part2 = wrongOrder.runParallel { fixUpdate(it) }.sumOf { u -> u[u.size / 2] }
        check(part2 == 6142)
    }
}