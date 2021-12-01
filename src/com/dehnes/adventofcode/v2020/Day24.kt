package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day24 {

    val alleSteps = listOf("ne", "nw", "w", "sw", "se", "e")
    val space = File("resources/2020/day24.txt").readLines().map { line ->
        line.toList().fold(emptyList()) { acc: List<String>, c ->
            val lastOrNull = acc.lastOrNull()
            if (lastOrNull == "s" || lastOrNull == "n") {
                acc.subList(0, acc.size - 1) + (lastOrNull + c)
            } else {
                acc + c.toString()
            }
        }
    }.fold(emptyMap<Pair<Int, Int>, Boolean>()) { acc, line ->
        var currentPos = 0 to 0
        line.forEach { step ->
            currentPos += mapPos(currentPos, step)
        }
        acc + (currentPos to !acc.getOrElse(currentPos) { false })
    }.toMutableMap()

    @Test
    fun main() {
        println(space.count { e -> e.value }) // 488

        repeat(100) {
            val (yRange, xRange) = spaceToScan()
            yRange.grow(2).flatMap { y -> xRange.grow(2).map { y to it } }.filter { pos ->
                val blackNCount = alleSteps.map { s ->
                    space.getOrElse(pos + mapPos(pos, s)) { false }
                }.count { it }
                if (space.getOrElse(pos) { false }) {
                    blackNCount == 0 || blackNCount > 2
                } else {
                    blackNCount == 2
                }
            }.forEach { pos ->
                space[pos] = !space.getOrElse(pos) { false }
            }
        }

        println(space.count { e -> e.value }) // 4118
    }

    private fun spaceToScan(): Pair<IntRange, IntRange> {
        var yRange = 0..0
        var xRange = 0..0
        space.forEach { (pos, _) ->
            if (pos.first < yRange.first) {
                yRange = pos.first..yRange.last
            }
            if (pos.first > yRange.last) {
                yRange = yRange.first..pos.first
            }
            if (pos.second < xRange.first) {
                xRange = pos.second..xRange.last
            }
            if (pos.second > xRange.last) {
                xRange = xRange.first..pos.second
            }
        }
        return yRange to xRange
    }

    private fun mapPos(currentPos: Pair<Int, Int>, step: String) = (currentPos.first % 2 == 0).let { fromEven ->
        when {
            !fromEven && step == "ne" -> 1 to 1
            !fromEven && step == "nw" -> 1 to 0
            !fromEven && step == "se" -> -1 to 1
            !fromEven && step == "sw" -> -1 to 0
            fromEven && step == "ne" -> 1 to 0
            fromEven && step == "nw" -> 1 to -1
            fromEven && step == "se" -> -1 to 0
            fromEven && step == "sw" -> -1 to -1
            step == "e" -> 0 to 1
            step == "w" -> 0 to -1
            else -> error("")
        }
    }

    operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = this.first + o.first to this.second + o.second
    fun IntRange.grow(w: Int) = this.first - w..this.last + w
}