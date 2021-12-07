package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

class Day07 {
    val input = File("resources/2021/day07.txt").readLines().first()
        .split(",").map { it.toInt() }

    @Test
    fun run() {
        assertEquals(
            343 to 340987L,
            costsForAllHeights(input) { it }.minByOrNull { it.second }
        )
        assertEquals(
            478 to 96987874L,
            costsForAllHeights(input) { (it * (it + 1)) / 2 }.minByOrNull { it.second }
        )
    }

    private fun costsForAllHeights(input: List<Int>, costFn: (Int) -> Int) =
        (input.minOf { it }..input.maxOf { it }).map { it to costForHeight(input, it, costFn) }

    private fun costForHeight(input: List<Int>, targetHeight: Int, costFn: (Int) -> Int) =
        input.fold(0L) { acc, pos ->
            acc + costFn((targetHeight - pos).absoluteValue)
        }

}