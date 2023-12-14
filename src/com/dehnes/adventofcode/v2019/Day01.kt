package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day01 {

    val masses = getLines().map { it.toLong() }

    @Test
    fun main() {
        check(3325342L == masses.sumOf { (it / 3) - 2 })

        check(4985158L == masses.sumOf { mass ->
            var sum = 0L
            var remainingMass = mass
            while (true) {
                val f = (remainingMass / 3) - 2
                if (f <= 0) {
                    break
                }
                sum += f
                remainingMass = f
            }
            sum
        })

    }

}