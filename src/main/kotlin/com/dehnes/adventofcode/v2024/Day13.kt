package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.MathUtils.solveForTwoUnknowns
import com.dehnes.adventofcode.utils.ParserUtils.getText
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test

class Day13 {

    private val machines = getText().split("\n\n").map {
        val (l1, l2, l3) = it.split("\n")
        Machine(a = l1.parseLine(), b = l2.parseLine(), prize = l3.parseLine())
    }

    @Test
    fun run() {
        check(36870L == solve(0))
        check(78101482023732L == solve(10000000000000L))
    }

    private fun solve(add: Long) = machines.mapNotNull { m ->
        solveForTwoUnknowns(
            a1 = m.a.x.toDouble(),
            b1 = m.b.x.toDouble(),
            c1 = m.prize.x.toDouble() + add,
            a2 = m.a.y.toDouble(),
            b2 = m.b.y.toDouble(),
            c2 = m.prize.y.toDouble() + add,
        )
    }.filter { (a, b) -> a % 1 == 0.0 && b % 1 == 0.0 }.sumOf { (a, b) -> a.toLong() * 3 + b.toLong() }

    data class Machine(val a: PointInt, val b: PointInt, val prize: PointInt)

    private fun String.parseLine() = split(": ")[1].split(", ").let { (l, r) ->
        PointInt(l.drop(2).toInt(), r.drop(2).toInt())
    }
}
