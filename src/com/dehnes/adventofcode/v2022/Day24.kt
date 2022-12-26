package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.math.absoluteValue

class Day24 {

    @Test
    fun run() {
        val gridTmp = mutableListOf<Array<GridPoint>>()
        inputLines(24).forEachIndexed { index, line ->
            if (!line.contains("###")) {
                line.toList().drop(1).dropLast(1).map { c ->
                    when (c) {
                        '.' -> GridPoint()
                        '>' -> GridPoint(mutableListOf(Blizzard(0 to 1, c)))
                        'v' -> GridPoint(mutableListOf(Blizzard(1 to 0, c)))
                        '<' -> GridPoint(mutableListOf(Blizzard(0 to -1, c)))
                        '^' -> GridPoint(mutableListOf(Blizzard(-1 to 0, c)))
                        else -> error("")
                    }
                }.let {
                    gridTmp.add(it.toTypedArray())
                }
            }
        }
        val grid = gridTmp.toTypedArray()

        val maxY = grid.size - 1
        val maxX = grid[0].size - 1

        val stepsPart1 = walkValey(
            -1 to 0,
            maxY to maxX,
            grid,
            maxY,
            maxX
        ) + 1

        expectThat(stepsPart1) isEqualTo 242

        val stepsPart2 =walkValey(
            maxY + 1 to maxX,
            0 to 0,
            grid,
            maxY,
            maxX
        )

        val stepsPart3 = walkValey(
            -1 to 0,
            maxY to maxX,
            grid,
            maxY,
            maxX)

        val final = stepsPart1 + stepsPart2 + stepsPart3

        expectThat(final) isEqualTo 720
    }

    private fun walkValey(
        startPos: Point,
        target: Pair<Int, Int>,
        grid: Array<Array<GridPoint>>,
        maxY: Int,
        maxX: Int,
    ): Int {
        val keep = 10000
        var minute = 0
        var takenPaths = listOf(
            Me(startPos, mutableListOf())
        )
        while (true) {
            minute++
            if (takenPaths.any { me -> me.pos == target }) {
                break
            }
            // move all blizzards
            grid.forEachIndexed { y, line ->
                line.forEachIndexed { x, gridPoint ->
                    val (toBeMoved, alreadyMoved) = gridPoint.blizzards.toList().partition { it.movedAt != minute }
                    gridPoint.blizzards.clear()
                    gridPoint.blizzards.addAll(alreadyMoved)
                    toBeMoved.forEach { b ->
                        var nextPos = (y to x) + b.direction
                        if (nextPos.first < 0) {
                            nextPos = maxY to nextPos.second
                        } else if (nextPos.first > maxY) {
                            nextPos = 0 to nextPos.second
                        } else if (nextPos.second < 0) {
                            nextPos = nextPos.first to maxX
                        } else if (nextPos.second > maxX) {
                            nextPos = nextPos.first to 0
                        }
                        b.movedAt = minute
                        grid[nextPos.first][nextPos.second].blizzards.add(b)
                    }
                }
            }

            // move all paths
            val newMes = mutableListOf<Me>()
            takenPaths.forEach { me ->
                val candidatePositions = grid.candidatePositions(me.pos)
                candidatePositions.forEach { c ->
                    newMes.add(
                        me.copy(
                            pos = c,
                            previous = me.previous + MoveAction(c)
                        )
                    )
                }
                if (grid.getOrNull(me.pos.first)?.getOrNull(me.pos.second)?.blizzards?.isEmpty() != false) {
                    newMes.add(
                        me.copy(
                            previous = me.previous + WaitAction
                        )
                    )
                }
            }
            takenPaths = newMes.distinctBy { it.pos }

            // keep only N-me'
            if (takenPaths.size > keep) {
                val sorted = takenPaths.sortedBy { it.pos.distanceTo(target) }
                takenPaths = sorted.subList(0, keep)
            }
        }

        val path = takenPaths.first { me -> me.pos == target }
        return path.previous.size
    }

    fun Point.distanceTo(p: Point) = (p.first - this.first).absoluteValue + (p.second - this.second).absoluteValue

    fun Array<Array<GridPoint>>.candidatePositions(pos: Point): MutableList<Point> {
        val candidatePositions = mutableListOf<Point>()
        val maxY = this.size - 1
        val maxX = this[0].size - 1
        // up?
        if (pos.first > 0 && this[pos.first - 1][pos.second].blizzards.isEmpty()) {
            candidatePositions.add(pos.first - 1 to pos.second)
        }
        // down ?
        if (pos.first < maxY && this[pos.first + 1][pos.second].blizzards.isEmpty()) {
            candidatePositions.add(pos.first + 1 to pos.second)
        }
        // right?
        if (pos.first in (0..maxY) && pos.second < maxX && this[pos.first][pos.second + 1].blizzards.isEmpty()) {
            candidatePositions.add(pos.first to pos.second + 1)
        }
        // left?
        if (pos.first in (0..maxY) && pos.second > 0 && this[pos.first][pos.second - 1].blizzards.isEmpty()) {
            candidatePositions.add(pos.first to pos.second - 1)
        }

        return candidatePositions
    }
}

data class Me(
    var pos: Point,
    val previous: List<Action>
)

sealed class Action
data class MoveAction(
    val dstPos: Point
) : Action()

object WaitAction : Action() {
    override fun toString(): String {
        return "WaitAction"
    }
}

data class GridPoint(
    val blizzards: MutableList<Blizzard> = mutableListOf()
)

data class Blizzard(
    val direction: Point,
    val directionChar: Char,
    var movedAt: Int = 0
)