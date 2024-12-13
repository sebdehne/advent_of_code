package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.MathUtils.gcd
import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day10 {

    val map = mutableListOf<Pair<Long, Long>>()

    init {
        getText().split("\n").forEachIndexed { lineIndex, lineStr ->
            lineStr.toList().forEachIndexed { colIndex, c ->
                if (c == '#') {
                    map.add(lineIndex.toLong() to colIndex.toLong())
                }
            }
        }
    }

    @Test
    fun part1() {
        val asteroidsToAsteroidsLines = mutableMapOf<Pair<Long, Long>, List<List<Pair<Long, Long>>>>()
        map.forEach { candidate ->
            asteroidsToAsteroidsLines[candidate] = calculateAsteroidsLines(candidate, map)
        }

        val station = asteroidsToAsteroidsLines.maxByOrNull { it.value.size }!!.key
        val asteroidsLinesForStation = asteroidsToAsteroidsLines[station]!!
        check(asteroidsLinesForStation.size == 230)

        val asteroidsClockWise = asteroidsLinesForStation.toMutableList()
        val vaporized = mutableListOf<Pair<Long, Long>>()
        while (asteroidsClockWise.any { it.isNotEmpty() }) {
            asteroidsClockWise.forEachIndexed { index, asteroids ->
                if (asteroids.isNotEmpty()) {
                    vaporized.add(asteroids.first())
                    asteroidsClockWise[index] = asteroids.drop(1)
                }
            }
        }

        val part2 = vaporized[199].let { it.second * 100 + it.first }
        check(part2 == 1205L)
    }

    private fun Pair<Long, Long>.distance(other: Pair<Long, Long>) =
            (other.first - this.first).absoluteValue + (other.second - this.second).absoluteValue

    private fun calculateAsteroidsLines(candidate: Pair<Long, Long>, map: List<Pair<Long, Long>>) =
            map.fold(emptyMap<Pair<Long, Long>, List<Pair<Long, Long>>>()) { acc, other ->
                if (candidate == other) {
                    acc
                } else {
                    val slope = candidate.slopeTo(other)
                    acc + (slope to acc.getOrElse(slope) { emptyList() } + other)
                }
            }.map { (slope, asteroids) ->
                slope to asteroids.sortedBy { candidate.distance(it) }
            }.sortedWith { o1, o2 -> compareSlope(o1.first, o2.first) }.map { it.second }


    private fun compareSlope(left: Pair<Long, Long>, other: Pair<Long, Long>) = when {
        left == other -> 0
        left.quadrant() != other.quadrant() -> left.quadrant().compareTo(other.quadrant())
        else -> {
            if (left.first == 0L || left.second == 0L) {
                -1
            } else if (other.first == 0L || other.second == 0L) {
                1
            } else {
                left.angle().compareTo(other.angle())
            }
        }
    }

    private fun Pair<Long, Long>.angle() = this.first.toDouble().div(this.second.toDouble())

    private fun Pair<Long, Long>.quadrant() = when {
        this.first == -1L && this.second == 0L -> 0
        this.first == 0L && this.second == 1L -> 1
        this.first == 1L && this.second == 0L -> 2
        this.first == 0L && this.second == -1L -> 3
        this.first.sign < 0 && this.second.sign > 0 -> 0
        this.first.sign > 0 && this.second.sign > 0 -> 1
        this.first.sign > 0 && this.second.sign < 0 -> 2
        this.first.sign < 0 && this.second.sign < 0 -> 3
        else -> error("")
    }

    private fun Pair<Long, Long>.slopeTo(o: Pair<Long, Long>) = when {
        this.first == o.first -> when {
            this.second < o.second -> 0L to 1L
            this.second > o.second -> 0L to -1L
            else -> 0L to 0L
        }
        this.second == o.second -> when {
            this.first < o.first -> 1L to 0L
            this.first > o.first -> -1L to 0L
            else -> error("")
        }
        else -> (o.first - this.first to o.second - this.second).simplify()
    }

    private fun Pair<Long, Long>.simplify() = gcd(this.first.absoluteValue, this.second.absoluteValue).let { gcd ->
        this.first / gcd to this.second / gcd
    }



}

