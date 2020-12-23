package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test

class Day23 {

    @Test
    fun part1Array() {
        val input = "389125467"
        val list = input.toList().map { it.toString().toInt() }
        val result = play(list, 10, 8)
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
        val max = input.maxOrNull()!!

        val cupToNextNeighbor = IntArray(input.size + 1)
        input.windowed(2) { (a, b) ->
            cupToNextNeighbor[a] = b
        }
        cupToNextNeighbor[input.last()] = input.first()
        val pickedUp = IntArray(3)

        var current = input.first()
        repeat(moves) {
            pickedUp[0] = cupToNextNeighbor[current]
            pickedUp[1] = cupToNextNeighbor[pickedUp[0]]
            pickedUp[2] = cupToNextNeighbor[pickedUp[1]]
            cupToNextNeighbor[current] = cupToNextNeighbor[pickedUp[2]]

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

            cupToNextNeighbor[pickedUp[2]] = cupToNextNeighbor[dstValue]
            cupToNextNeighbor[dstValue] = pickedUp[0]
            current = cupToNextNeighbor[current]
        }

        return (1 until resultLength).fold(listOf(cupToNextNeighbor[1])) { acc, _ ->
            acc + cupToNextNeighbor[acc.last()]
        }
    }
}