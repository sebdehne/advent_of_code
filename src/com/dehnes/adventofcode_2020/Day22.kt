package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day22 {

    val player1: List<Int>
    val player2: List<Int>

    init {
        val (p1, p2) = File("resources/day22.txt").readText().split("\n\n").map { str ->
            val lines = str.split("\n")
            lines.slice(1 until lines.size).map { it.toInt() }
        }
        player1 = p1
        player2 = p2
    }

    @Test
    fun part1() {
        val (r1, r2) = play(player1, player2, false)
        println("Part1: " + scoreDeck(if (r1.isNotEmpty()) r1 else r2)) // 33098
    }

    @Test
    fun part2() {
        val (r1, r2) = play(player1, player2, true)
        println("Part2: " + scoreDeck(if (r1.isNotEmpty()) r1 else r2)) // 35055
    }

    fun scoreDeck(deck: List<Int>): Long {
        var m = 1L
        return deck.reversed().fold(0L) { acc, l ->
            acc + (l * m++)
        }
    }

    fun play(p1Input: List<Int>, p2Input: List<Int>, recursive: Boolean): Pair<List<Int>, List<Int>> {
        var (p1, p2) = p1Input to p2Input
        var alreadyPlayed = emptyList<Pair<Long, Long>>()
        while (p1.isNotEmpty() && p2.isNotEmpty()) {

            val s1 = scoreDeck(p1)
            val s2 = scoreDeck(p2)
            val p1Winner = if (recursive && alreadyPlayed.contains(s1 to s2)) {
                true
            } else {
                alreadyPlayed = alreadyPlayed + (s1 to s2)
                val one = p1[0]
                val two = p2[0]

                val oneRemainig = p1.size - 1
                val twoRemainig = p2.size - 1
                if (recursive && one <= oneRemainig && two <= twoRemainig) {
                    play(p1.slice(1..one), p2.slice(1..two), recursive).first.isNotEmpty()
                } else {
                    one > two
                }
            }

            if (p1Winner) {
                p1 = p1.slice(1 until p1.size) + p1[0] + p2[0]
                p2 = p2.slice(1 until p2.size)
            } else {
                p2 = p2.slice(1 until p2.size) + p2[0] + p1[0]
                p1 = p1.slice(1 until p1.size)
            }
        }

        return p1 to p2
    }

}