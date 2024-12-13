package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Combinations.getAllCombinations
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.runParallel
import org.junit.jupiter.api.Test
import java.util.*

class Day07 {

    @Test
    fun run() {
        val puzzle = getLines().map {
            val (testValue, inputs) = it.split(": ")
            testValue.toLong() to inputs.split(" ").map { it.toLong() }
        }

        // part1
        check(1298300076754 == puzzle.runParallel { (testValue, inputs) ->
            testValue to getPossibleSolutions(testValue, inputs, Operator.plus, Operator.multi)
        }.filter { it.second.isNotEmpty() }.sumOf { it.first })

        // part2
        check(248427118972289 == puzzle.runParallel { (testValue, inputs) ->
            testValue to getPossibleSolutions(testValue, inputs, Operator.plus, Operator.multi, Operator.concat)
        }.filter { it.second.isNotEmpty() }.sumOf { it.first })
    }

    enum class Operator(val calc: (l: Long, r: Long) -> Long) {
        plus({ l, r -> l + r }),
        multi({ l, r -> l * r }),
        concat({ l, r -> (l.toString() + r.toString()).toLong() }),
    }

    private fun getPossibleSolutions(testValue: Long, inputs: List<Long>, vararg ops: Operator): List<List<Operator>> =
        getAllCombinations(inputs.size - 1, ops.toList()).filter { inputs.solve(it) == testValue }

    private fun List<Long>.solve(ops: List<Operator>): Long {
        val remainingOps = LinkedList(ops)
        return this.reduce { acc, i -> remainingOps.removeFirst()!!.calc(acc, i) }
    }
}