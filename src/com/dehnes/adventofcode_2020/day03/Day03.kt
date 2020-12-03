package com.dehnes.adventofcode_2020.day03

import java.io.File
import java.nio.charset.Charset

data class Point(
        val line: Int,
        val offset: Int
)

fun List<String>.isTree(pos: Point) = this[pos.line][pos.offset] == '#'

fun List<String>.moveTo(current: Point, slope: Pair<Int, Int>) = (current.line + slope.first).let { newLineOffset ->
    if (newLineOffset < 0 || newLineOffset >= this.size) {
        null
    } else {
        Point(newLineOffset, (current.offset + slope.second) % this[newLineOffset].length)
    }
}

fun main() {
    val map = File("resources/day03.txt")
            .readLines(Charset.defaultCharset())

    val resultPart1 = testSlope(map, 1 to 3)
    println("Result part1: $resultPart1")

    val resultPart2 = listOf(
            1 to 1,
            1 to 3,
            1 to 5,
            1 to 7,
            2 to 1
    ).map { pair -> testSlope(map, pair) }.reduce { acc, i -> acc * i }
    println("Result part2: $resultPart2")
}

private fun testSlope(map: List<String>, slope: Pair<Int, Int>): Long {
    var pos = Point(0, 0)
    var treeCount = 0L
    while (true) {
        pos = map.moveTo(pos, slope) ?: break
        if (map.isTree(pos))
            treeCount += 1
    }
    return treeCount
}