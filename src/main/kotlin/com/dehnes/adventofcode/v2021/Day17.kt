package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test

class Day17 {
    //val targetX = 20..30
    //val targetY = -10..-5
    val targetX = 265..287
    val targetY = -103..-58

    @Test
    fun run() {
        val xCandidates = findXCandidates()

        val initials = mutableSetOf<Pair<Int, Int>>()
        var candidateY = targetY.first
        var highestY = Integer.MIN_VALUE
        while (true) {
            for (canX in xCandidates) {
                val points = calculatePath(canX, candidateY)
                if (points != null) {
                    initials.add(canX to candidateY)
                    if (points.any { it.second > highestY }) {
                        highestY = points.maxOf { it.second }
                    }
                }
            }
            candidateY++
            if (candidateY > 1000) {
                break
            }
        }

        check(highestY == 5253)
        check(initials.size == 1770)
    }

    private fun findXCandidates(): List<Int> {
        val xCandidates = mutableListOf<Int>()
        var x = targetX.last
        while (x > 0) {
            when (isUsableX(x, targetX)) {
                -1 -> break
                0 -> xCandidates.add(x)
            }
            x--
        }
        return xCandidates
    }

    private fun calculatePath(x: Int, y: Int): List<Pair<Int, Int>>? {
        val result = mutableListOf<Pair<Int, Int>>()
        var velocity = x to y
        result.add(velocity)
        while (true) {
            val pos = result.last()
            if (pos.first in targetX && pos.second in targetY) {
                return result
            }
            if (pos.first > targetX.last || pos.second < targetY.first) {
                return null
            }
            velocity = (if (velocity.first > 0) velocity.first - 1 else 0) to (velocity.second - 1)
            result.add((pos.first + velocity.first) to (pos.second + velocity.second))
        }
    }

    private fun isUsableX(x: Int, targetX: IntRange): Int {
        var dst = 0
        var substract = 0
        while (true) {
            if (substract > x) {
                return -1
            }
            dst += x - substract++
            if (dst > targetX.last) {
                return 1
            }
            if (dst in targetX) {
                return 0
            }
        }
    }

}