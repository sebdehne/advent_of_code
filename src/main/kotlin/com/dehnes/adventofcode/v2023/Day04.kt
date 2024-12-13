package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import kotlin.math.pow

class Day04 {
    val data = getLines().map {
        val (left, right) = it.split(":", limit = 2).last().split("|")
        (left.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }) to (right.trim().split(" ")
            .filter { it.isNotBlank() }.map { it.toInt() })
    }

    @Test
    fun part1() {
        val points = data.map { game ->
            val (winning, have) = game
            val base = have.filter { n -> n in winning }
            if (base.isNotEmpty()) {
                2.0.pow(base.size.toDouble() - 1).toInt()
            } else 0
        }

        val part1 = points.sum()
        check(part1 == 28538)
    }


    @Test
    fun part2() {
        val gameToMatchingNumbers = data.mapIndexed { gameIndex: Int, pair: Pair<List<Int>, List<Int>> ->
            val (winning, have) = pair
            (gameIndex + 1) to have.filter { n -> n in winning }.size
        }.toMap()

        val owning = mutableMapOf<Int, Int>()
        gameToMatchingNumbers.forEach {
            owning[it.key] = 1
        }

        gameToMatchingNumbers.forEach { (game, winning) ->
            val cardsOwned = owning[game]!!
            var cnt = winning
            var current = game + 1
            while (cnt-- > 0) {
                owning[current] = owning[current]!! + cardsOwned
                current++
            }
        }

        check(owning.values.sum() == 9425061)
    }

}
