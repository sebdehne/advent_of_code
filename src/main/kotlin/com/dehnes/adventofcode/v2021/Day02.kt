package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day02 {

    val input = getLines().map {
        it.split(" ").let {
            it[0] to it[1].toInt()
        }
    }

    fun instructions1(direction: String, currentPos: IntArray) = when (direction) {
        "forward" -> intArrayOf(1, 0)
        "down" -> intArrayOf(0, 1)
        "up" -> intArrayOf(0, -1)
        else -> error("")
    }

    fun instructions2(direction: String, currentPos: IntArray) = when (direction) {
        "forward" -> intArrayOf(1, currentPos[2], 0)
        "down" -> intArrayOf(0, 0, 1)
        "up" -> intArrayOf(0, 0, -1)
        else -> error("")
    }

    fun calc(
        input: List<Pair<String, Int>>,
        startPos: IntArray,
        instructions: (String, IntArray) -> IntArray
    ) =
        input.fold(startPos) { acc, pair ->
            val mult = instructions(pair.first, acc)
            acc.mapIndexed { index, i ->
                i + (mult[index] * pair.second)
            }.toIntArray()
        }.let { it[0] * it[1] }

    @Test
    fun run() {
        check(calc(input, intArrayOf(0, 0), this::instructions1) == 1692075)

        check(calc(input, intArrayOf(0, 0, 0), this::instructions2) == 1749524700)
    }

}