package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day07 {
    val input = getLines().first().split(",").map { it.toInt() }

    @Test
    fun run() {
        check(costsForAllHeights(input) { it }.minByOrNull { it.second } == 343 to 340987L)
        check(costsForAllHeights(input) { (it * (it + 1)) / 2 }.minByOrNull { it.second } == 478 to 96987874L)
    }

    private fun costsForAllHeights(input: List<Int>, costFn: (Int) -> Int) =
        (input.minOf { it }..input.maxOf { it }).map { it to costForHeight(input, it, costFn) }

    private fun costForHeight(input: List<Int>, targetHeight: Int, costFn: (Int) -> Int) =
        input.fold(0L) { acc, pos ->
            acc + costFn((targetHeight - pos).absoluteValue)
        }

}