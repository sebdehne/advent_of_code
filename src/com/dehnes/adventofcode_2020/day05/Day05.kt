package com.dehnes.adventofcode_2020.day05

import java.io.File
import java.lang.Integer.parseInt

val seats = File("resources/day05.txt").readLines()
        .map { parseInt(it.replace("[FL]".toRegex(), "0").replace("[RB]".toRegex(), "1"), 2) }
        .sorted()

fun main() {
    println("Part1: highest seat ID: ${seats.last()}")

    val mySeat = seats.filterIndexed { index, seat ->
        index < seats.size - 2 && seat + 1 != seats[index + 1]
    }.single() + 1
    println("Part2: my seat=$mySeat")
}