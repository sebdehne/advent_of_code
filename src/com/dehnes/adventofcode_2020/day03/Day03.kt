package com.dehnes.adventofcode_2020.day03

import java.io.File
import java.nio.charset.Charset

data class Point(
        val line: Int,
        val offset: Int
)

fun List<String>.moveTo(current: Point, slope: Pair<Int, Int>) = (current.line + slope.first).let { newLineOffset ->
    if (newLineOffset < 0 || newLineOffset >= this.size) {
        null
    } else {
        Point(newLineOffset, (current.offset + slope.second) % this[newLineOffset].length)
    }
}

fun List<String>.calcTreesForSlope(slope: Pair<Int, Int>): Long {
    var pos = Point(0, 0)
    var treeCount = 0L
    while (true) {
        pos = this.moveTo(pos, slope) ?: break
        treeCount += if (this[pos.line][pos.offset] == '#') 1 else 0
    }
    return treeCount
}

fun main() {
    val map = File("resources/day03.txt")
            .readLines(Charset.defaultCharset())
    val calcTreesForSlope = { slope: Pair<Int, Int> ->
        map.calcTreesForSlope(slope)
    }

    println("Result part1: ${calcTreesForSlope(1 to 3)}")

    val resultPart2 = listOf(
            1 to 1,
            1 to 3,
            1 to 5,
            1 to 7,
            2 to 1
    ).map(calcTreesForSlope).reduce { acc, i -> acc * i }
    println("Result part2: $resultPart2")
}