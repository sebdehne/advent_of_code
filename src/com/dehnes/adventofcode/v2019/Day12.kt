package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.MathUtils.lcm
import com.dehnes.adventofcode.v2022.Day18.Companion.XYZ
import com.dehnes.adventofcode.v2022.Day18.Companion.plus
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day12 {
    val positions = inputLines(12).map { line ->
        line.replace("<", "").replace(">", "").split(",").map { it.trim().split("=")[1].toInt() }.let {
            XYZ(
                it[0],
                it[1],
                it[2],
            )
        }
    }

    @Test
    fun part1() {
        val moons = positions.map {
            Moon(it, XYZ(0, 0, 0))
        }

        var steps = 0
        while(steps++ < 1000) {
            calcStep(moons)
            moons.forEach { m ->
                m.pos += m.vel
            }
        }

        val part1 = moons.sumOf {
            (it.pos.x.absoluteValue
                    + it.pos.y.absoluteValue
                    + it.pos.z.absoluteValue) * (it.vel.x.absoluteValue
                    + it.vel.y.absoluteValue
                    + it.vel.z.absoluteValue)
        }

        check(part1 == 7636)
    }

    @Test
    fun part2() {
        val moons = positions.map {
            Moon(it, XYZ(0, 0, 0))
        }

        val xRepeats = mutableSetOf<List<Int>>()
        val yRepeats = mutableSetOf<List<Int>>()
        val zRepeats = mutableSetOf<List<Int>>()
        var xRepeatsAfter: Long? = null
        var yRepeatsAfter: Long? = null
        var zRepeatsAfter: Long? = null

        var steps = 0L
        while(true) {
            if (xRepeatsAfter == null) {
                if (!xRepeats.add(moons.flatMap { listOf(it.pos.x, it.vel.x) })) {
                    xRepeatsAfter = steps
                }
            }
            if (yRepeatsAfter == null) {
                if (!yRepeats.add(moons.flatMap { listOf(it.pos.y, it.vel.y) })) {
                    yRepeatsAfter = steps
                }
            }
            if (zRepeatsAfter == null) {
                if (!zRepeats.add(moons.flatMap { listOf(it.pos.z, it.vel.z) })) {
                    zRepeatsAfter = steps
                }
            }

            if (xRepeatsAfter != null && yRepeatsAfter != null && zRepeatsAfter != null) {
                break
            }

            calcStep(moons)
            moons.forEach { m ->
                m.pos += m.vel
            }
            steps++
        }

        val firstRepeat = lcm(lcm(xRepeatsAfter!!, yRepeatsAfter!!), zRepeatsAfter!!)

        check(firstRepeat == 281691380235984)

    }

    private fun calcStep(moons: List<Moon>) {
        listOf(
            0 to 1,
            0 to 2,
            0 to 3,
            1 to 2,
            1 to 3,
            2 to 3,
        ).forEach { (left, right ) ->
            val lMoon = moons[left]
            val rMoon = moons[right]

            if (lMoon.pos.x != rMoon.pos.x) {
                if (lMoon.pos.x < rMoon.pos.x) {
                    lMoon.vel += XYZ(1, 0, 0)
                    rMoon.vel += XYZ(-1, 0, 0)
                } else {
                    lMoon.vel += XYZ(-1, 0, 0)
                    rMoon.vel += XYZ(1, 0, 0)
                }
            }
            if (lMoon.pos.y != rMoon.pos.y) {
                if (lMoon.pos.y < rMoon.pos.y) {
                    lMoon.vel += XYZ(0, 1, 0)
                    rMoon.vel += XYZ(0, -1, 0)
                } else {
                    lMoon.vel += XYZ(0, -1, 0)
                    rMoon.vel += XYZ(0, 1, 0)
                }
            }
            if (lMoon.pos.z != rMoon.pos.z) {
                if (lMoon.pos.z < rMoon.pos.z) {
                    lMoon.vel += XYZ(0, 0, 1)
                    rMoon.vel += XYZ(0,0, -1)
                } else {
                    lMoon.vel += XYZ(0, 0, -1)
                    rMoon.vel += XYZ(0, 0, 1)
                }
            }

        }
    }

    companion object {
        data class Moon(
            var pos: XYZ,
            var vel: XYZ
        )
    }

}

