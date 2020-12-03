package com.dehnes.adventofcode_2020.day03

import java.io.File
import java.nio.charset.Charset

data class Point(
        val line: Int,
        val offset: Int
)

fun List<String>.isTree(pos: Point) = this[pos.line][pos.offset] == '#'

fun List<String>.moveTo(current: Point, lineDelta: Int, offsetDelta: Int) = (current.line + lineDelta).let { newLineOffset ->
    if (newLineOffset < 0 || newLineOffset >= this.size) {
        null
    } else {
        Point(newLineOffset, (current.offset + offsetDelta) % this[newLineOffset].length)
    }
}

fun main() {
    val map = File("resources/day03.txt")
            .readLines(Charset.defaultCharset())

    val resultPart1 = testSlope(map, 1, 3)
    println("Result part1: $resultPart1")

    val resultPart2 = listOf(
            1 to 1,
            1 to 3,
            1 to 5,
            1 to 7,
            2 to 1
    ).map { pair ->
        testSlope(map, pair.first, pair.second).also { println("Slope $pair => $it") }
    }.reduce { acc, i -> acc * i }

    println("Result part2: $resultPart2")
}

private fun testSlope(map: List<String>, lineDelta: Int, offsetDelta: Int): Long {
    var pos = Point(0, 0)
    var treeCount = 0L
    while (true) {
        pos = map.moveTo(pos, lineDelta, offsetDelta) ?: break
        if (map.isTree(pos))
            treeCount += 1
    }
    return treeCount
}