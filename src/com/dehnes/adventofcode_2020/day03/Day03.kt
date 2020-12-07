package com.dehnes.adventofcode_2020.day03

import java.io.File

val map = File("resources/day03.txt").readLines()

fun calcTreesForSlope(slope: Pair<Int, Int>): Long {
    var pos = 0 to 0
    var treeCount = 0L
    while (true) {
        pos = (pos.first + slope.first).let { newLineOffset ->
            if (newLineOffset < map.size) {
                newLineOffset to (pos.second + slope.second) % map[newLineOffset].length
            } else {
                return treeCount
            }
        }
        treeCount += if (map[pos.first][pos.second] == '#') 1 else 0
    }
}

fun main() {
    println("Result part1: ${calcTreesForSlope(1 to 3)}")

    val slopesToTest = listOf(1 to 1, 1 to 3, 1 to 5, 1 to 7, 2 to 1)
    println("Result part2: ${slopesToTest.map(::calcTreesForSlope).reduce { acc, i -> acc * i }}")
}