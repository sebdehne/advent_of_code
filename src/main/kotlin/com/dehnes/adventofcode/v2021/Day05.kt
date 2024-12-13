package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.lang.Integer.max
import kotlin.math.sign

class Day05 {

    val input = getLines()
        .map { it.split("\\D".toRegex()).filterNot { it.isBlank() } }
        .map { (it[0].toInt() to it[1].toInt()) to (it[2].toInt() to it[3].toInt()) }

    @Test
    fun test() {
        check(countIntersections(input.filter { it.isStrait() }) == 4421)
        check(countIntersections(input) == 18674)
    }

    fun countIntersections(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
        val area = Array(1000) { IntArray(1000) }

        input.forEach { (from, to) ->
            val xDirection = (to.first - from.first).sign
            val yDirection = (to.second - from.second).sign

            for (s in (from to to).length() downTo 0) {
                area[from.second + s * yDirection][from.first + s * xDirection]++
            }
        }

        return area.fold(0) { acc, line -> acc + line.filter { it > 1 }.size }
    }

    private fun Pair<Int, Int>.sort() = if (this.first < this.second) this else (this.second to this.first)
    private fun Pair<Int, Int>.delta() = this.sort().let { it.second - it.first }
    private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.length() = max(
        (this.first.first to this.second.first).delta(),
        (this.first.second to this.second.second).delta()
    )

    private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.isStrait() =
        this.first.first == this.second.first || this.first.second == this.second.second


}