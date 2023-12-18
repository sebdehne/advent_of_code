package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Direction.Companion.down
import com.dehnes.adventofcode.utils.Direction.Companion.left
import com.dehnes.adventofcode.utils.Direction.Companion.right
import com.dehnes.adventofcode.utils.Direction.Companion.up
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import java.lang.Integer.max
import java.lang.Integer.min

class Day18 {

    data class Instruction(val d: Direction, val len: Int, val col: String)
    data class Line(val xRange: IntRange, val yRange: IntRange, val originalDirection: Direction) {

        companion object {
            fun create(from: PointInt, to: PointInt, d: Direction) = Line(
                (min(from.x, to.x))..(max(from.x, to.x)),
                (min(from.y, to.y))..(max(from.y, to.y)),
                d,
            )
        }

        fun len() = (xRange.last - xRange.first) + (yRange.last - yRange.first)

        fun isHorizontal() = yRange.first == yRange.last
        fun isVertical() = xRange.first == xRange.last

        fun isPoint() = xRange.first == xRange.last && yRange.first == yRange.last

        fun merge(l: Line) = if (isHorizontal() && l.isHorizontal()) {
            Line(
                (min(l.xRange.first, xRange.first))..(max(l.xRange.last, xRange.last)),
                yRange,
                originalDirection
            )
        } else if (isVertical() && l.isVertical()) {
            Line(
                xRange,
                (min(yRange.first, l.yRange.first))..(max(yRange.last, l.yRange.last)),
                originalDirection
            )
        } else {
            error("")
        }

        operator fun contains(p: PointInt) = p.x in xRange && p.y in yRange
        operator fun contains(l: Line) = if (isHorizontal() && l.isHorizontal()) {
            (l.xRange.first in xRange || l.xRange.last in xRange) && yRange.first == l.yRange.first
        } else if (isVertical() && l.isVertical()) {
            (l.yRange.first in yRange || l.yRange.last in yRange) && xRange.first == l.xRange.first
        } else {
            false
        }
    }

    private fun IntRange.isFullyInside(smaller: IntRange) = smaller.first in this && smaller.last in this


    @Test
    fun run() {

        val linesPart1 = instructionsToLines(getLines().map { line ->
            val (dir, l, color) = line.split(" ")
            val d = when (dir) {
                "U" -> up
                "D" -> down
                "L" -> left
                "R" -> right
                else -> error("")
            }
            val col = color.replace("(", "").replace(")", "")
            Instruction(d, l.toInt(), col)
        })

        check(solve(linesPart1) == 52055L)

        val linesPart2 = instructionsToLines(getLines().map { line ->
            val (_, _, color) = line.split(" ")
            val col = color.replace("(", "").replace(")", "")

            val dis = col.substring(1, 6).toInt(radix = 16)

            val d = when (col[6]) {
                '3' -> up
                '1' -> down
                '2' -> left
                '0' -> right
                else -> error("")
            }
            Instruction(d, dis, col)
        })

        check(solve(linesPart2) == 67622758357096L)
    }

    private fun instructionsToLines(instructions: List<Instruction>): MutableSet<Line> {
        var current = PointInt(0, 0)
        return instructions.map {
            val s = current
            current = PointInt(
                when (it.d) {
                    right -> current.x + it.len
                    left -> current.x - it.len
                    else -> current.x
                },
                when (it.d) {
                    up -> current.y - it.len
                    down -> current.y + it.len
                    else -> current.y
                }
            )
            Line.create(s, current, it.d)
        }.toMutableSet()
    }

    private fun solve(lines: MutableSet<Line>): Long {

        /*
         * Reduce the entire loop to a single square by
         * shifting lines down/up/right/left, counting the
         * areas which have been added/removed along the way
         *
         * note: this was before I knew about the Shoelace formula
         */

        // count the area of the lines themselves
        var area = lines.fold(0L) { acc, line ->
            acc + (line.len())
        }

        val searched = mutableSetOf<Line>()

        while (lines.size > 4) {

            val line = lines.first { it !in searched }
            searched.add(line)

            if (line.isHorizontal()) {
                val y = line.yRange.last

                val right = run {
                    val rPoint = PointInt(line.xRange.last, y)
                    lines.first { it != line && rPoint in it }
                }
                val left = run {
                    val rPoint = PointInt(line.xRange.first, y)
                    lines.first { it != line && rPoint in it }
                }

                if (right.yRange.last > y && left.yRange.last > y) {
                    // move down

                    val constrainedBYs = if (left.yRange.last > right.yRange.last) {
                        listOf(right)
                    } else if (left.yRange.last == right.yRange.last) {
                        listOf(right, left)
                    } else {
                        listOf(left)
                    }

                    val canMove = constrainedBYs.none { constrainedBY ->
                        val endPoint = PointInt(
                            constrainedBY.xRange.first,
                            constrainedBY.yRange.last
                        )

                        val next = lines.first { it != constrainedBY && endPoint in it }
                        line.xRange.isFullyInside(next.xRange)
                    }

                    if (canMove) {
                        searched.clear()

                        val endY = min(right.yRange.last, left.yRange.last)

                        val dY = (endY - line.yRange.first).toLong()
                        val dX = (line.len() - 1).toLong()
                        if (line.originalDirection == Direction.right) {
                            area += (dY * dX)
                        } else {
                            area -= (dY * dX)
                            area -= (dY * 2)
                        }

                        val replacement = Line(
                            line.xRange,
                            endY..endY,
                            line.originalDirection
                        )
                        val rightReplacement = Line(
                            right.xRange,
                            endY..right.yRange.last,
                            right.originalDirection
                        )
                        val leftReplacement = Line(
                            left.xRange,
                            endY..left.yRange.last,
                            left.originalDirection
                        )

                        lines.remove(line)
                        lines.remove(right)
                        lines.remove(left)
                        if (!rightReplacement.isPoint())
                            lines.add(rightReplacement)
                        if (!leftReplacement.isPoint())
                            lines.add(leftReplacement)

                        val replacementFinal = lines
                            .filter {
                                it in replacement
                            }
                            .fold(replacement) { acc, l ->
                                lines.remove(l)
                                acc.merge(l)
                            }
                        lines.add(replacementFinal)
                    }

                    // cannot move
                } else if (right.yRange.first < y && left.yRange.first < y) {
                    // move up

                    val constrainedBYs = if (left.yRange.first < right.yRange.first) {
                        listOf(right)
                    } else if (left.yRange.first == right.yRange.first) {
                        listOf(right, left)
                    } else {
                        listOf(left)
                    }

                    val cannotMove = constrainedBYs.any { constrainedBY ->
                        val endPoint = PointInt(
                            constrainedBY.xRange.last,
                            constrainedBY.yRange.first
                        )

                        val next = lines.first { it != constrainedBY && endPoint in it }
                        line.xRange.isFullyInside(next.xRange)
                    }

                    if (cannotMove) {
                        // cannot move
                    } else {
                        searched.clear()
                        val endY = max(right.yRange.first, left.yRange.first)

                        val dY = (line.yRange.first - endY).toLong()
                        val dX = (line.len() - 1).toLong()
                        if (line.originalDirection == Direction.left) {
                            area += (dY * dX)
                        } else {
                            area -= (dY * dX)
                            area -= (dY * 2)
                        }


                        val replacement = Line(
                            line.xRange,
                            endY..endY,
                            line.originalDirection
                        )
                        val rightReplacement = Line(
                            right.xRange,
                            right.yRange.first..endY,
                            right.originalDirection,
                        )
                        val leftReplacement = Line(
                            left.xRange,
                            left.yRange.first..endY,
                            left.originalDirection
                        )

                        lines.remove(line)
                        lines.remove(right)
                        lines.remove(left)
                        if (!rightReplacement.isPoint())
                            lines.add(rightReplacement)
                        if (!leftReplacement.isPoint())
                            lines.add(leftReplacement)

                        val replacementFinal = lines
                            .filter { it in replacement }
                            .fold(replacement) { acc, l ->
                                lines.remove(l)
                                acc.merge(l)
                            }
                        lines.add(replacementFinal)
                    }

                }
            }

            // vertical
            else {
                val x = line.xRange.last

                val up = run {
                    val uPoint = PointInt(x, line.yRange.first)
                    lines.first { it != line && uPoint in it }
                }
                val down = run {
                    val dPoint = PointInt(x, line.yRange.last)
                    lines.first { it != line && dPoint in it }
                }

                if (up.xRange.last > x && down.xRange.last > x) {
                    // move right

                    val constrainedBYs = if (up.xRange.last > down.xRange.last) {
                        listOf(down)
                    } else if (up.xRange.last == down.xRange.last) {
                        listOf(up, down)
                    } else {
                        listOf(up)
                    }

                    val cannotMove = constrainedBYs.any { constrainedBY ->
                        val endPoint = PointInt(
                            constrainedBY.xRange.last,
                            constrainedBY.yRange.first
                        )

                        val next = lines.first { it != constrainedBY && endPoint in it }
                        line.yRange.isFullyInside(next.yRange)
                    }

                    if (cannotMove) {
                        // cannot move
                    } else {

                        searched.clear()

                        val endX = min(up.xRange.last, down.xRange.last)

                        val dY = (line.len() - 1).toLong()
                        val dX = (endX - line.xRange.first).toLong()
                        if (line.originalDirection == Direction.up) {
                            area += (dY * dX)
                        } else {
                            area -= (dY * dX)
                            area -= (dX * 2)
                        }

                        val replacement = Line(
                            endX..endX,
                            line.yRange,
                            line.originalDirection,
                        )
                        val upReplacement = Line(
                            endX..up.xRange.last,
                            up.yRange,
                            up.originalDirection,
                        )
                        val downReplacement = Line(
                            endX..down.xRange.last,
                            down.yRange,
                            down.originalDirection,
                        )

                        lines.remove(line)
                        lines.remove(up)
                        lines.remove(down)
                        if (!upReplacement.isPoint())
                            lines.add(upReplacement)
                        if (!downReplacement.isPoint())
                            lines.add(downReplacement)

                        val replacementFinal = lines
                            .filter {
                                it in replacement
                            }
                            .fold(replacement) { acc, l ->
                                lines.remove(l)
                                acc.merge(l)
                            }
                        lines.add(replacementFinal)
                    }
                } else if (up.xRange.first < x && down.xRange.first < x) {
                    // move left


                    val constrainedBYs = if (up.xRange.first < down.xRange.first) {
                        listOf(down)
                    } else if (up.xRange.first == down.xRange.first) {
                        listOf(up, down)
                    } else {
                        listOf(up)
                    }

                    val cannotMove = constrainedBYs.any { constrainedBY ->
                        val endPoint = PointInt(
                            constrainedBY.xRange.first,
                            constrainedBY.yRange.first
                        )

                        val next = lines.first { it != constrainedBY && endPoint in it }
                        line.yRange.isFullyInside(next.yRange)
                    }


                    if (cannotMove) {
                        // cannot move
                    } else {

                        searched.clear()
                        val endX = max(up.xRange.first, down.xRange.first)

                        val dY = (line.len() - 1).toLong()
                        val dX = (line.xRange.first - endX).toLong()
                        if (line.originalDirection == Direction.down) {
                            area += (dY * dX)
                        } else {
                            area -= (dY * dX)
                            area -= (dX * 2)
                        }

                        val replacement = Line(
                            endX..endX,
                            line.yRange,
                            line.originalDirection,
                        )
                        val upReplacement = Line(
                            up.xRange.first..endX,
                            up.yRange,
                            up.originalDirection,
                        )
                        val downReplacement = Line(
                            down.xRange.first..endX,
                            down.yRange,
                            down.originalDirection,
                        )

                        lines.remove(line)
                        lines.remove(up)
                        lines.remove(down)
                        if (!upReplacement.isPoint())
                            lines.add(upReplacement)
                        if (!downReplacement.isPoint())
                            lines.add(downReplacement)

                        val replacementFinal = lines
                            .filter { it in replacement }
                            .fold(replacement) { acc, l ->
                                lines.remove(l)
                                acc.merge(l)
                            }
                        lines.add(replacementFinal)
                    }
                }
            }
        }

        val first = lines.first()
        val next = lines.first { can ->
            val point = PointInt(can.xRange.last, can.yRange.last)
            can != first && point in can
        }
        area += (first.len() - 1).toLong() * (next.len() - 1).toLong()

        return area
    }

}

