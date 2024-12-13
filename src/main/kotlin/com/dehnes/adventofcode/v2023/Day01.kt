package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day01 {

    @Test
    fun test() {
        check(calc(part2 = false) == 56042L)
        check(calc(part2 = true) == 55358L)
    }

    private fun calc(part2: Boolean) = getLines().map {
        val indexToDigit = mutableListOf<Pair<Int, Int>>()
        it.forEachIndexed { index, c ->
            if (c.isDigit()) {
                indexToDigit.add(index to c.toString().toInt())
            }
        }
        if (part2) {
            listOf(
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight",
                "nine",
            ).forEachIndexed { value, word ->
                var offset = 0
                while (true) {
                    val i = it.indexOf(word, offset)
                    if (i != -1) {
                        indexToDigit.add(i to (value + 1))
                        offset = i + 1
                        continue
                    }
                    break
                }
            }
        }


        indexToDigit.sortedBy { it.first }.let {
            "${it.first().second}${it.last().second}".toLong()
        }
    }.sum()
}
