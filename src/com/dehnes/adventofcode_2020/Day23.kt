package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test

class Day23 {

    val list = "614752839".toList().map { it.toString().toInt() }

    @Test
    fun part1() {
        val result = play(list, 100, 8)
        println(result.joinToString("")) // 89372645
    }

    @Test
    fun part2() {
        val updatedInput = list.toMutableList()
        updatedInput += (list.size + 1)..1_000_000
        val (first, two) = play(updatedInput, 10_000_000, 2)

        println(first.toLong() * two.toLong()) // 21273394210
    }


    private fun play(input: List<Int>, moves: Int, resultLength: Int): List<Int> {
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
                    dstValue = input.size
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