package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.plus
import com.dehnes.adventofcode.utils.times
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day03 {

    val lines = getLines().map { lineStr ->
        lineStr.split(",").fold(listOf(0 to 0)) { acc, directionAndLength ->
            acc + (acc.last() + multiplier(directionAndLength[0]) * directionAndLength.substring(1).toInt())
        }
    }

    val lineA = lines[0]
    val lineB = lines[1]

    @Test
    fun main() {
        val intersections = lineA.windowed(2).fold(emptyList<Pair<Int, Int>>()) { acc, a ->
            lineB.windowed(2).fold(acc) { acc2, b ->
                calcIntersection(a, b)?.let { acc2 + it } ?: acc2
            }
        }.filterNot { it == 0 to 0 }
        check(intersections.minOfOrNull { it.first.absoluteValue + it.second.absoluteValue } == 870)

        val stepsA = intersections.map { stepsToIntersection(lineA, it) }
        val stepsB = intersections.map { stepsToIntersection(lineB, it) }
        check(stepsA.zip(stepsB).minOfOrNull { (a, b) -> a + b } == 13698)
    }

    fun stepsToIntersection(line: List<Pair<Int, Int>>, intersection: Pair<Int, Int>) = line.windowed(2).fold(0) { steps, (a, b) ->
        steps + if (listOf(a, b).isHorizontal()) {
            if (intersection.line() == a.line() && columns(a, b).contains(intersection.column())) {
                return steps + (intersection.column() - a.column()).absoluteValue
            }
            (b.column() - a.column()).absoluteValue
        } else {
            if (intersection.column() == a.column() && lines(a, b).contains(intersection.line())) {
                return steps + (intersection.line() - a.line()).absoluteValue
            }
            (b.line() - a.line()).absoluteValue
        }
    }

    fun calcIntersection(
            vecA: List<Pair<Int, Int>>,
            vecB: List<Pair<Int, Int>>
    ): Pair<Int, Int>? {
        val (aStart, aEnd) = if (vecA.isHorizontal()) vecA else vecB
        val (bStart, bEnd) = if (vecA.isHorizontal()) vecB else vecA

        return if (
                vecA.isHorizontal().xor(vecB.isHorizontal()) &&
                columns(aStart, aEnd).contains(bStart.column()) &&
                lines(bStart, bEnd).contains(aStart.line())) {
            aStart.line() to bStart.column()
        } else null
    }

    fun multiplier(dir: Char) = when (dir) {
        'R' -> 0 to 1
        'L' -> 0 to -1
        'U' -> 1 to 0
        'D' -> -1 to 0
        else -> error("")
    }

    fun Pair<Int, Int>.contains(i: Int) = (i in this.first..this.second) || (i in this.second..this.first)

    fun List<Pair<Int, Int>>.isHorizontal() = this[0].line() == this[1].line()
    fun columns(a: Pair<Int, Int>, b: Pair<Int, Int>) = a.second to b.second
    fun lines(a: Pair<Int, Int>, b: Pair<Int, Int>) = a.first to b.first
    fun Pair<Int, Int>.column() = this.second
    fun Pair<Int, Int>.line() = this.first

}