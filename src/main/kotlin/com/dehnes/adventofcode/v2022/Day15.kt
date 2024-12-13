package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.Point
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day15 {

    @Test
    fun run() {
        val areas = getLines().map { line ->
            val parts = line.split(" ", ",", "=", ":")
            val sensor = (parts[3].toInt() to parts[6].toInt())
            val beacon = (parts[13].toInt() to parts[16].toInt())
            Area(sensor, beacon, sensor.distance(beacon))
        }

        val left = areas.minOf { it.center.first - it.radius }
        val right = areas.maxOf { it.center.first + it.radius }

        check(
            run(
                areas,
                2000000..2000000,
                left..right,
                false
            )
                    == 4748135L
        )

        check(
            run(
                areas,
                0..4000000,
                0..4000000,
                true
            )
                    == 13743542639657L
        )

    }

    fun run(areas: List<Area>, yRange: IntRange, xRange: IntRange, isPart2: Boolean): Long {
        var counter = 0L

        yRange.forEach { y ->

            var x = xRange.first
            while (x in xRange) {
                val next = x to y

                areas.firstOrNull { it.contains(next) }?.let { a ->
                    counter++
                    val yDelta = (a.center.second - next.second).absoluteValue
                    val xDelta = a.center.first - next.first
                    val xDelta2 = a.radius - yDelta
                    val move = xDelta + xDelta2
                    x += move
                    counter += move
                    if (next == a.beacon || (x to y) == a.beacon) {
                        counter--
                    }
                } ?: run {
                    if (isPart2) {
                        return next.first.toLong() * 4000000 + next.second.toLong()
                    }
                }

                x++
            }
        }
        return counter
    }

    companion object {
        data class Area(
            val center: Point,
            val beacon: Point,
            val radius: Int,
        ) {
            fun contains(p: Point): Boolean {
                return center.distance(p) <= radius
            }
        }

        fun Point.distance(other: Point): Int {
            val yDistance = (this.first - other.first).absoluteValue
            val xDistance = (this.second - other.second).absoluteValue
            return xDistance + yDistance
        }

    }
}

