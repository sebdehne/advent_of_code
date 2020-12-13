package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test

class Day13 {
    val input = (
            "29,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,521,x,x,x,x,x,x,x,23,x,x,x,x,13,x,x,x,17,x,x,x,x,x,x,x,x,x,x,x,x,x,601,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,19"
            ).split(",").map { if (it == "x") -1 else it.toLong() }

    @Test
    fun main() {
        val timestamp = 1002461
        val (buss, timeToWait) = input.filter { it != -1L }.map { b -> b to b - (timestamp % b) }.minByOrNull { it.second }!!
        println("Part1: ${buss * timeToWait}") // 4207

        val alignAt = input.mapIndexed { index, s -> s to index.toLong() }.filter { it.first >= 0 }.fold(1L to 0L) { acc, b ->
            lcd(acc.first, b.first) to alignAt(acc.second, acc.first, b.first, b.second)
        }.second
        println("Part2: $alignAt") // 725850285300475
    }

    fun alignAt(initOffset: Long, n1: Long, n2: Long, n2Delta: Long): Long = generateSequence(0) { it + 1 }
            .map { it * n1 + initOffset }
            .first { (it + n2Delta) % n2 == 0L }

    fun lcd(n1: Long, n2: Long): Long = (n1 * n2) / gcd(n1, n2)
    fun gcd(n1: Long, n2: Long): Long = if (n2 != 0L) gcd(n2, n1 % n2) else n1
}
