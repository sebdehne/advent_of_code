package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day05 {
    val seats = File("resources/2020/day05.txt").readLines()
            .map { Integer.parseInt(it.replace("[FL]".toRegex(), "0").replace("[RB]".toRegex(), "1"), 2) }
            .sorted()

    @Test
    fun run() {
        println("Part1: highest seat ID: ${seats.last()}")

        val mySeat = seats.filterIndexed { index, seat ->
            index < seats.size - 2 && seat + 1 != seats[index + 1]
        }.single() + 1
        println("Part2: my seat=$mySeat")
    }
}