package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.RangeUtils.overlaps
import com.dehnes.adventofcode.v2022.Day18
import org.junit.jupiter.api.Test

class Day22 {

    private var idGen = 1
    private var bricks = getLines().map { line ->
        val (from, to) = line.split("~").map { c ->
            val (x, y, z) = c.split(",")
            Day18.Companion.XYZ(x.toInt(), y.toInt(), z.toInt())
        }
        Brick(
            idGen++,
            from.x..to.x,
            from.y..to.y,
            from.z..to.z,
        )
    }.sortedBy { it.z.first }.associateBy { it.id }

    data class Brick(val id: Int, val x: IntRange, val y: IntRange, val z: IntRange) {
        fun moveDown() = copy(z = (z.first - 1)..<z.last)
        fun moveUp() = copy(z = (z.first + 1)..(z.last + 1))
        fun conflicts(other: Brick) = x.overlaps(other.x) && y.overlaps(other.y) && z.overlaps(other.z)
    }

    @Test
    fun run() {
        // part1
        val compacted = compact(bricks)
        check(canBeRemoved(compacted) == 426)

        // part2
        var totalFallen = 0L
        compacted.forEach { (bId, _) ->
            val removed = compacted.filterNot { it.key == bId }
            val compactedAfterRemove = compact(removed)
            totalFallen += compactedAfterRemove.count { (bId, b) ->
                val before = removed[bId]!!
                before != b
            }
        }
        check(totalFallen == 61920L)
    }

    private fun canBeRemoved(compacted: Map<Int, Brick>) = compacted.count { (bId, b) ->
        val up = b.moveUp()
        compacted.values.filterNot { it.id == bId }.filter { it.conflicts(up) }.all { s ->
            val d = s.moveDown()
            compacted.values.filterNot { it.id == s.id || it.id == bId }.any { it.conflicts(d) }
        }
    }

    private fun compact(bricks: Map<Int, Brick>): Map<Int, Brick> {
        val copy = mutableMapOf<Int, Brick>()
        bricks.values.forEach { b -> copy[b.id] = b.copy() }

        val brickIds = copy.keys.toMutableSet()

        var didSomething = true
        while (didSomething) {
            didSomething = false
            brickIds.forEach { id ->
                val b = copy.remove(id)!!
                if (b.z.first > 1) {
                    val moved = b.moveDown()
                    val conflictsWith = copy.values.firstOrNull { it.conflicts(moved) }
                    if (conflictsWith == null) {
                        didSomething = true
                        copy[id] = moved
                    } else {
                        copy[id] = b
                    }
                } else {
                    copy[id] = b
                }
            }
        }

        return copy
    }


}