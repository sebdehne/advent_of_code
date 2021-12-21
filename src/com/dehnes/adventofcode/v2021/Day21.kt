package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21 {

    @Test
    fun part1() {
        var player1 = Pair(4, 0)
        var player2 = Pair(2, 0)

        var dieValue = 0
        var dieCount = 0
        val dieRoll = {
            (dieValue.mod(100) + 1).apply {
                dieValue = this
                dieCount++
            }
        }

        val roll = {
            dieRoll() + dieRoll() + dieRoll()
        }

        while (true) {
            player1 = (((player1.first - 1) + roll()).mod(10) + 1).let { newPos ->
                newPos to (player1.second + newPos)
            }
            if (player1.second >= 1000) break
            player2 = (((player2.first - 1) + roll()).mod(10) + 1).let { newPos ->
                newPos to (player2.second + newPos)
            }
            if (player2.second >= 1000) break
        }

        assertEquals(908595, dieCount * player2.second)
    }

    @Test
    fun part2() {
        val result = calcWinningUniverses(Pair(4, 0), Pair(2, 0))
        assertEquals(91559198282731L, result.first)
    }

    private fun calcWinningUniverses(player1: Pair<Int, Int>, player2: Pair<Int, Int>): Pair<Long, Long> {
        val r1 = playPlayer(player1)
        val (remaining1, won1) = r1.partition { it.second < 21 }
        val remaining1combinations = remaining1.groupingBy { it }.eachCount()

        var wonUnisP1 = won1.size.toLong()

        val r2 = playPlayer(player2)
        val (remaining2, won2) = r2.partition { it.second < 21 }
        val remaining2combinations = remaining2.groupingBy { it }.eachCount()

        var wonUnisP2 = won2.size.toLong() * remaining1.size

        remaining1combinations.forEach { e1 ->
            remaining2combinations.forEach { e2 ->
                val multiplier = e1.value * e2.value
                val p1 = e1.key
                val p2 = e2.key

                val l = calcWinningUniverses(p1, p2)
                wonUnisP1 += l.first * multiplier
                wonUnisP2 += l.second * multiplier
            }
        }

        return wonUnisP1 to wonUnisP2
    }

    private fun playPlayer(player: Pair<Int, Int>) = dieOutcomes.map { outcome ->
        (((player.first - 1) + outcome).mod(10) + 1).let {
            it to player.second + it
        }
    }

    private val dieOutcomes: List<Int>

    init {
        val r = mutableListOf<Int>()
        for (r1 in 1..3) {
            for (r2 in 1..3) {
                for (r3 in 1..3) {
                    r.add(r1 + r2 + r3)
                }
            }
        }
        dieOutcomes = r
    }

}