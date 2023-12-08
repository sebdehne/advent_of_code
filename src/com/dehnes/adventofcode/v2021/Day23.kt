package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

class Day23 {

    val a = 1
    val b = 10
    val c = 100
    val d = 1000
    val destinations = mapOf(
        a to 2,
        b to 4,
        c to 6,
        d to 8,
    )

    @Test
    fun run() {
        val grid = Array(3) { IntArray(11) }
        grid[1][2] = c
        grid[2][2] = d
        grid[1][4] = c
        grid[2][4] = d
        grid[1][6] = a
        grid[2][6] = b
        grid[1][8] = b
        grid[2][8] = a

        val minScore = calcPart1(grid)

        check(minScore == 18282L) // 2min, 10 seconds
    }

    @Test
    fun run2() {
        val grid = Array(5) { IntArray(11) }
        grid[1][2] = c
        grid[2][2] = d
        grid[3][2] = d
        grid[4][2] = d

        grid[1][4] = c
        grid[2][4] = c
        grid[3][4] = b
        grid[4][4] = d

        grid[1][6] = a
        grid[2][6] = b
        grid[3][6] = a
        grid[4][6] = b

        grid[1][8] = b
        grid[2][8] = a
        grid[3][8] = c
        grid[4][8] = a

        val minScore = calcPart2(grid)

        check(minScore == 50132L) // 1min, 43 seconds
    }

    private fun calcPart1(grid: Array<IntArray>): Long? {
        val findPossibleMoves = findPossibleMovesPart1(grid)
        return findPossibleMoves.mapNotNull { m ->
            val (updatedGrid, score) = makeMove(grid, m)
            if (isDonePart1(updatedGrid)) {
                score
            } else {
                calcPart1(updatedGrid)?.let { score + it }
            }
        }.minByOrNull { it }
    }

    private fun calcPart2(grid: Array<IntArray>): Long? {
        val findPossibleMoves = findPossibleMovesPart2(grid)
        return findPossibleMoves.mapNotNull { m ->
            val (updatedGrid, score) = makeMove(grid, m)
            if (isDonePart2(updatedGrid)) {
                score
            } else {
                calcPart2(updatedGrid)?.let { score + it }
            }
        }.minByOrNull { it }
    }

    private fun isDonePart1(grid: Array<IntArray>): Boolean = destinations.toList().all { (p, dst) ->
        grid[1][dst] == p && grid[2][dst] == p
    }

    private fun isDonePart2(grid: Array<IntArray>): Boolean = destinations.toList().all { (p, dst) ->
        grid[1][dst] == p && grid[2][dst] == p && grid[3][dst] == p && grid[4][dst] == p
    }

    private fun makeMove(grid: Array<IntArray>, move: Move): Pair<Array<IntArray>, Long> {
        val copy = Array(grid.size) { IntArray(11) }
        copy.forEachIndexed { index, ints ->
            System.arraycopy(grid[index], 0, ints, 0, ints.size)
        }
        val from = copy[move.from.first][move.from.second]
        copy[move.from.first][move.from.second] = 0
        copy[move.to.first][move.to.second] = from
        return copy to (from * move.steps).toLong()
    }

    private fun findPossibleMovesPart1(grid: Array<IntArray>): List<Move> {
        val result = mutableListOf<Move>()

        for (index in grid[0].indices) {
            val p = grid[0][index]

            if (p != 0) {
                val dst = destinations[p]!!

                val pathFree = if (index < dst) {
                    ((index + 1)..dst).sumOf { grid[0][it] } == 0
                } else {
                    (dst until index).sumOf { grid[0][it] } == 0
                }

                if (!pathFree) {
                    continue
                }

                // can move to 1 row?
                if (grid[1][dst] == 0 && grid[2][dst] == p) {
                    result.add(
                        Move(
                            0 to index,
                            1 to dst,
                            (dst - index).absoluteValue + 1
                        )
                    )
                }

                // can move to 2 row?
                else if (grid[2][dst] == 0 && grid[1][dst] == 0) {
                    result.add(
                        Move(
                            0 to index,
                            2 to dst,
                            (dst - index).absoluteValue + 2
                        )
                    )
                }
            }
        }

        for (dst in destinations.values) {
            for (row in 1..2) {
                val p = grid[row][dst]

                if (p == 0) {
                    continue
                }

                val shouldMove = if (row == 1)
                    destinations[p]!! != dst || destinations[grid[2][dst]] != dst
                else
                    destinations[p]!! != dst

                if (shouldMove) {

                    // try left
                    var to = dst - 1
                    while (to >= 0) {

                        if (to !in destinations.values) {
                            val pathFree = (min(dst, to)..max(dst, to)).sumOf { grid[0][it] } == 0
                            if (pathFree) {
                                result.add(
                                    Move(
                                        row to dst,
                                        0 to to,
                                        row + (dst - to)
                                    )
                                )
                            } else {
                                break
                            }
                        }
                        to--
                    }

                    // try right
                    to = dst + 1
                    while (to < 11) {
                        if (to !in destinations.values) {
                            val pathFree = (min(dst, to)..max(dst, to)).sumOf { grid[0][it] } == 0
                            if (pathFree) {
                                result.add(
                                    Move(
                                        row to dst,
                                        0 to to,
                                        row + (to - dst)
                                    )
                                )
                            } else {
                                break
                            }
                        }
                        to++
                    }

                }

                break
            }
        }

        return result
    }

    private fun findPossibleMovesPart2(grid: Array<IntArray>): List<Move> {
        val result = mutableListOf<Move>()

        // hallway -> dst
        for (index in grid[0].indices) {
            val p = grid[0][index]

            if (p != 0) {
                val dst = destinations[p]!!

                val pathFree = if (index < dst) {
                    ((index + 1)..dst).sumOf { grid[0][it] } == 0
                } else {
                    (dst until index).sumOf { grid[0][it] } == 0
                }

                if (!pathFree) {
                    continue
                }

                // can move to 1 row?
                if (grid[1][dst] == 0 && grid[2][dst] == p && grid[3][dst] == p && grid[4][dst] == p) {
                    result.add(
                        Move(
                            0 to index,
                            1 to dst,
                            (dst - index).absoluteValue + 1
                        )
                    )
                }

                // can move to 2 row?
                else if (grid[1][dst] == 0 && grid[2][dst] == 0 && grid[3][dst] == p && grid[4][dst] == p) {
                    result.add(
                        Move(
                            0 to index,
                            2 to dst,
                            (dst - index).absoluteValue + 2
                        )
                    )
                }

                // can move to 3 row?
                else if (grid[1][dst] == 0 && grid[2][dst] == 0 && grid[3][dst] == 0 && grid[4][dst] == p) {
                    result.add(
                        Move(
                            0 to index,
                            3 to dst,
                            (dst - index).absoluteValue + 3
                        )
                    )
                }

                // can move to 4 row?
                else if (grid[1][dst] == 0 && grid[2][dst] == 0 && grid[3][dst] == 0 && grid[4][dst] == 0) {
                    result.add(
                        Move(
                            0 to index,
                            4 to dst,
                            (dst - index).absoluteValue + 4
                        )
                    )
                }
            }
        }

        // room -> hallway
        for (dst in destinations.values) {
            for (row in 1..4) {
                val p = grid[row][dst]

                if (p == 0) {
                    continue
                }

                val shouldMove = when(row) {
                    1 -> destinations[p] != dst
                            || destinations[grid[2][dst]] != dst
                            || destinations[grid[3][dst]] != dst
                            || destinations[grid[4][dst]] != dst
                    2 -> destinations[p] != dst
                            || destinations[grid[3][dst]] != dst
                            || destinations[grid[4][dst]] != dst
                    3 -> destinations[p] != dst
                            || destinations[grid[4][dst]] != dst
                    4 -> destinations[p] != dst
                    else -> error("")
                }

                if (shouldMove) {

                    // try left
                    var to = dst - 1
                    while (to >= 0) {

                        if (to !in destinations.values) {
                            val pathFree = (min(dst, to)..max(dst, to)).sumOf { grid[0][it] } == 0
                            if (pathFree) {
                                result.add(
                                    Move(
                                        row to dst,
                                        0 to to,
                                        row + (dst - to)
                                    )
                                )
                            } else {
                                break
                            }
                        }
                        to--
                    }

                    // try right
                    to = dst + 1
                    while (to < 11) {
                        if (to !in destinations.values) {
                            val pathFree = (min(dst, to)..max(dst, to)).sumOf { grid[0][it] } == 0
                            if (pathFree) {
                                result.add(
                                    Move(
                                        row to dst,
                                        0 to to,
                                        row + (to - dst)
                                    )
                                )
                            } else {
                                break
                            }
                        }
                        to++
                    }

                }

                break
            }
        }

        return result
    }

    data class Move(
        val from: Pair<Int, Int>,
        val to: Pair<Int, Int>,
        val steps: Int
    )


}