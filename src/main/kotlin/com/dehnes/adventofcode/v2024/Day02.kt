package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day02 {

    @Test
    fun run() {
        check(eval(false) == 299)
        check(eval(true) == 364)
    }

    private fun eval(part2: Boolean): Int = getLines().count { line ->
        val r = line.split(" ").map { it.toInt() }
        r.isSafe() || (part2 && r.indices.any { i -> r.filterIndexed { i2, _ -> i2 != i }.isSafe() })
    }

    private fun List<Int>.isSafe(): Boolean {
        val decreasing = windowed(2).all { (l, r) -> l > r }
        val increasing = windowed(2).all { (l, r) -> l < r }
        val slowly = windowed(2).all { (l, r) -> (l - r).absoluteValue in 1..3 }
        return (decreasing || increasing) && slowly
    }
}
