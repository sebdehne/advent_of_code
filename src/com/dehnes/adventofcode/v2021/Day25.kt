package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day25 {

    @Test
    fun test() {
        var map = File("resources/2021/day25.txt").readLines().map { line ->
            line.toList().toCharArray()
        }.toTypedArray()

        var steps = 0L
        while (true) {
            steps++
            val (moves, updatedMap) = map.move()
            if (moves == 0L) {
                break
            }
            map = updatedMap
        }

        assertEquals(360, steps)

    }

    private fun Array<CharArray>.move(): Pair<Long, Array<CharArray>> {
        val (moves, afterEast) = this.moveEast()
        val (moves2, afterSouth) = afterEast.moveSouth()
        return (moves + moves2) to afterSouth
    }

    private fun Array<CharArray>.moveEast(): Pair<Long, Array<CharArray>> {
        var moves = 0L
        val copy = this.copy()
        this.forEachIndexed { iy, y ->
            y.forEachIndexed { ix, x ->
                if (x == '>') {
                    val next = this[iy][(ix + 1).mod(y.size)]
                    if (next == '.') {
                        // move
                        copy[iy][(ix + 1).mod(y.size)] = x
                        copy[iy][ix] = '.'
                        moves++
                    }
                }
            }
        }
        return moves to copy
    }

    private fun Array<CharArray>.moveSouth(): Pair<Long, Array<CharArray>> {
        var moves = 0L
        val copy = this.copy()
        this.forEachIndexed { iy, y ->
            y.forEachIndexed { ix, x ->
                if (x == 'v') {
                    val next = this[(iy + 1).mod(this.size)][ix]
                    if (next == '.') {
                        // move
                        copy[(iy + 1).mod(this.size)][ix] = x
                        copy[iy][ix] = '.'
                        moves++
                    }
                }
            }
        }
        return moves to copy
    }

    private fun Array<CharArray>.copy(): Array<CharArray> {
        val copy = Array(this.size) { CharArray(this[0].size) }
        this.forEachIndexed { index, ints ->
            System.arraycopy(ints, 0, copy[index], 0, ints.size)
        }
        return copy
    }
}