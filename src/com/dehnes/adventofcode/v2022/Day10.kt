package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day10 {

    @Test
    fun run() {
        var x = 1
        val signalStrengts = mutableListOf<Long>()
        val screen = Array(6) { CharArray(40) { '.' } }
        var crtPos = 0 to 0
        var currentCycle = 1L

        getLines().forEach { line: String ->
            val parts = line.split(" ")
            val value = if (parts.size > 1) parts[1].toInt() else 0

            val cyclesToAll = when {
                parts.first() == "noop" -> 1
                parts.first() == "addx" -> 2
                else -> error("")
            }

            repeat(cyclesToAll) {
                if (currentCycle >= 20 && (currentCycle - 20).mod(40) == 0) {
                    signalStrengts.add(currentCycle * x)
                }

                // draw
                screen[crtPos.second][crtPos.first] = if (crtPos.first in listOf(x - 1, x, x + 1)) '#' else '.'

                // move CRT
                crtPos += (if (crtPos.first == 39) (if (crtPos.second == 5) -39 to -5 else -39 to 1) else 1 to 0)

                currentCycle++
            }

            x += value
        }

        expectThat(signalStrengts.sum()) isEqualTo 14520

        // print screen == PZBGZEJB
        screen.forEach { println(it) }

    }
}