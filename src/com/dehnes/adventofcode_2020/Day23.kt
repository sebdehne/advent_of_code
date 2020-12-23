package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test

class Day23 {

    @Test
    fun part1Array() {
        val input = "614752839"
        val list = input.toList().map { it.toString().toInt() }
        val result = play(list, 100, 8)
        println(result.joinToString("")) // 89372645
    }

    @Test
    fun part2Array() {
        val input = "614752839"
        val list = input.toList().map { it.toString().toInt() }

        val max = list.maxOrNull()!!
        val updatedInput = list.toMutableList()
        for (n in (max + 1)..1_000_000) {
            updatedInput.add(n)
        }
        val (first, two) = play(updatedInput, 10_000_000, 2)

        println(first.toLong() * two.toLong())
    }


    private fun play(input: List<Int>, moves: Int, resultLength: Int): List<Int> {
        val cups = makeCircle(input)
        val max = input.maxOrNull()!!

        var current = input.first()
        repeat(moves) {
            val pickedUp = IntArray(3)
            pickedUp[0] = cups[current]!!
            pickedUp[1] = cups[pickedUp[0]]!!
            pickedUp[2] = cups[pickedUp[1]]!!
            cups[current] = cups[pickedUp[2]]!!

            var dstValue = current - 1
            while (true) {
                if (dstValue == 0) {
                    dstValue = max
                } else if (dstValue == pickedUp[0] || dstValue == pickedUp[1] || dstValue == pickedUp[2]) {
                    dstValue--
                } else {
                    break
                }
            }

            cups[pickedUp[2]] = cups[dstValue]!!
            cups[dstValue] = pickedUp[0]

            current = cups[current]!!
        }

        return (1 until resultLength).fold(listOf(cups[1]!!)) { acc, _ ->
            acc + cups[acc.last()]!!
        }
    }

    private fun makeCircle(l: List<Int>): HashMap<Int, Int> {
        val h = HashMap<Int, Int>()
        val headValue = l[0]
        h[headValue] = headValue

        var lastInserted = headValue
        (1 until l.size).forEach { i ->
            val value = l[i]
            h[value] = headValue
            h[lastInserted] = value
            lastInserted = value
        }

        return h
    }

}