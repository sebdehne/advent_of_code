package com.dehnes.adventofcode.v2019

import org.junit.jupiter.api.Test

class Day01 {

    val masses = inputLines(1).map { it.toLong() }

    @Test
    fun main() {
        println(masses.map { (it / 3) - 2 }.sum()) // 3325342

        println(masses.map { mass ->
            sequence {
                var remainingMass = mass
                while (true) {
                    val f = (remainingMass / 3) - 2
                    if (f <= 0) {
                        break
                    }
                    yield(f)
                    remainingMass = f
                }
            }.sum()
        }.sum()) // 4985158

    }

}