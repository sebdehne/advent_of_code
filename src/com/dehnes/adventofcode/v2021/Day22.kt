package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.lang.Integer.max
import java.lang.Integer.min

class Day22 {

    data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {

        fun inside(other: Cuboid) = x.inside(other.x) && y.inside(other.y) && z.inside(other.z)
        fun IntRange.inside(r: IntRange) = this.first in r && this.last in r
        fun IntRange.overlap(r: IntRange) = this.first in r || this.last in r || r.first in this || r.last in this
        fun interactsWith(other: Cuboid): Boolean = x.overlap(other.x) && y.overlap(other.y) && z.overlap(other.z)

        fun volume() =
            ((x.last - x.first) + 1).toLong() * ((y.last - y.first) + 1).toLong() * ((z.last - z.first) + 1).toLong()

        fun makeCutOut(other: Cuboid): List<Cuboid> {
            if (inside(other)) return emptyList()

            val top = if (y.last > other.y.last) {
                Cuboid(
                    x,
                    (other.y.last + 1)..y.last,
                    z
                )
            } else null

            val bottom = if (y.first < other.y.first) {
                Cuboid(
                    x,
                    y.first until other.y.first,
                    z
                )
            } else null

            val front = if (z.first < other.z.first) {
                Cuboid(
                    max(x.first, other.x.first)..x.last,
                    max(y.first, other.y.first)..min(y.last, other.y.last),
                    z.first until other.z.first
                )
            } else null

            val right = if (x.last > other.x.last) {
                Cuboid(
                    (other.x.last + 1)..x.last,
                    max(y.first, other.y.first)..min(y.last, other.y.last),
                    max(z.first, other.z.first)..z.last
                )
            } else null

            val back = if (z.last > other.z.last) {
                Cuboid(
                    x.first..min(other.x.last, x.last),
                    max(y.first, other.y.first)..min(y.last, other.y.last),
                    (other.z.last + 1)..z.last
                )
            } else null

            val left = if (x.first < other.x.first) {
                Cuboid(
                    x.first until other.x.first,
                    max(y.first, other.y.first)..min(y.last, other.y.last),
                    z.first..min(z.last, other.z.last)
                )
            } else null

            return listOfNotNull(top, bottom, right, left, front, back)
        }
    }

    @Test
    fun run() {
        val instructions = getLines().map(this::parseLine)

        check(calc(instructions.subList(0, 20)) == 610196L) // part1
        check(calc(instructions) == 1282401587270826L) // part2
    }

    private fun calc(instructions: List<Pair<Boolean, Cuboid>>) =
        instructions.fold(emptyList<Cuboid>()) { acc, (instr, cuboid) ->
            val (interacting, others) = acc.partition { it.interactsWith(cuboid) }

            val interactingExploded = interacting.fold(emptyList<Cuboid>()) { l, int ->
                l + int.makeCutOut(cuboid)
            }

            if (instr) {
                others + interactingExploded + cuboid
            } else {
                others + interactingExploded
            }
        }.sumOf { it.volume() }

    private fun parseLine(line: String) = line.split(" ").let {
        (it[0] == "on") to it[1].split(",").let {
            val x = it[0].substring(2).split("..").let {
                it[0].toInt() to it[1].toInt()
            }
            val y = it[1].substring(2).split("..").let {
                it[0].toInt() to it[1].toInt()
            }
            val z = it[2].substring(2).split("..").let {
                it[0].toInt() to it[1].toInt()
            }
            Cuboid(x.first..x.second, y.first..y.second, z.first..z.second)
        }
    }
}

