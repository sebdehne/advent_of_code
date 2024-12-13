package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.Point
import com.dehnes.adventofcode.utils.plus
import org.junit.jupiter.api.Test

class Day22 {

    @Test
    fun run() {
        val grid = mutableListOf<CharArray>()

        val instructions = mutableListOf<Instruction2>()
        var readingInstructions = false
        getLines().forEach { line ->

            if (!readingInstructions) {
                if (line.isBlank()) {
                    readingInstructions = true
                } else {
                    grid.add(line.toCharArray())
                }
            } else {
                val l = line.toList()
                val numberBuffer = mutableListOf<Char>()
                l.forEachIndexed { index, c ->
                    when (c) {
                        'R' -> {
                            if (numberBuffer.isNotEmpty()) {
                                instructions.add(Walk(numberBuffer.joinToString("").toInt()))
                                numberBuffer.clear()
                            }
                            instructions.add(Turn(1))
                        }

                        'L' -> {
                            if (numberBuffer.isNotEmpty()) {
                                instructions.add(Walk(numberBuffer.joinToString("").toInt()))
                                numberBuffer.clear()
                            }
                            instructions.add(Turn(-1))
                        }

                        else -> numberBuffer.add(c)
                    }
                }
                if (numberBuffer.isNotEmpty()) {
                    instructions.add(Walk(numberBuffer.joinToString("").toInt()))
                    numberBuffer.clear()
                }
            }
        }

        var pos = 0 to grid[0].indexOfFirst { it != ' ' }
        var direction = 0
        instructions.forEach { instruction ->
            if (instruction is Walk) {

                for (i in 0 until instruction.amount) {
                    var next = pos + direction.getStep()
                    if (grid.get(next) == ' ') {
                        // need to wrap

                        val wrapPos = when (direction) {
                            0 -> {
                                val line = grid[pos.first]
                                pos.first to (line.indexOfFirst { it != ' ' })
                            }

                            1 -> {
                                val targetLine = grid.indexOfFirst { line -> line.getOrElse(pos.second) { ' ' } != ' ' }
                                targetLine to pos.second
                            }

                            2 -> {
                                val line = grid[pos.first]
                                pos.first to (line.size - 1)
                            }

                            3 -> {
                                val targetLine = grid.indexOfLast { line -> line.getOrElse(pos.second) { ' ' } != ' ' }
                                targetLine to pos.second
                            }

                            else -> error("")
                        }

                        if (grid.get(wrapPos) == '#') break
                        next = wrapPos
                    }


                    if (grid.get(next) == '#') {
                        // abort
                        break
                    } else {
                        grid[next.first][next.second] = direction.toArrow()
                        pos = next
                    }
                }

            } else {
                val t = instruction as Turn
                direction = direction.turn(t)
            }

        }

        val password = (1000 * (pos.first + 1)) + (4 * (pos.second + 1)) + direction

        check(password == 43466)
    }

    @Test
    fun part2() {

        // real input
        val gridSize = 50 - 1
        val gridSpecs = mapOf(
            1 to Area(0 to 50, 49 to 99),
            2 to Area(0 to 100, 49 to 149),
            3 to Area(50 to 50, 99 to 99),
            4 to Area(100 to 0, 149 to 49),
            5 to Area(100 to 50, 149 to 99),
            6 to Area(150 to 0, 199 to 49),
        )
        val wrapSpecs = listOf(
            WrappingSpec(1, 2, 4) { p -> ((gridSize - p.first) to 0) to 0 },
            WrappingSpec(1, 3, 6) { p -> (p.second to 0) to 0 },

            WrappingSpec(2, 0, 5) { p -> ((gridSize - p.first) to gridSize) to 2 },
            WrappingSpec(2, 1, 3) { p -> (p.second to gridSize) to 2 },
            WrappingSpec(2, 3, 6) { p -> (gridSize to p.second) to 3 },

            WrappingSpec(3, 0, 2) { p -> (gridSize to (p.first)) to 3 },
            WrappingSpec(3, 2, 4) { p -> (0 to p.first) to 1 },

            WrappingSpec(4, 2, 1) { p -> ((gridSize - p.first) to 0) to 0 },
            WrappingSpec(4, 3, 3) { p -> (p.second to 0) to 0 },

            WrappingSpec(5, 0, 2) { p -> ((gridSize - p.first) to gridSize) to 2 },
            WrappingSpec(5, 1, 6) { p -> (p.second to gridSize) to 2 },

            WrappingSpec(6, 0, 5) { p -> (gridSize to p.first) to 3 },
            WrappingSpec(6, 1, 2) { p -> (0 to p.second) to 1 },
            WrappingSpec(6, 2, 1) { p -> (0 to p.first) to 1 },
        )

        val password = solvePart2(gridSpecs, wrapSpecs)

        check(password == 162155)
    }

    private fun solvePart2(
        gridSpecs: Map<Int, Area>,
        wrapSpecs: List<WrappingSpec>,
    ): Int {
        val grid = mutableListOf<CharArray>()

        val instructions = mutableListOf<Instruction2>()
        var readingInstructions = false
        getLines().forEach { line ->

            if (!readingInstructions) {
                if (line.isBlank()) {
                    readingInstructions = true
                } else {
                    grid.add(line.toCharArray())
                }
            } else {
                val l = line.toList()
                val numberBuffer = mutableListOf<Char>()
                l.forEachIndexed { index, c ->
                    when (c) {
                        'R' -> {
                            if (numberBuffer.isNotEmpty()) {
                                instructions.add(Walk(numberBuffer.joinToString("").toInt()))
                                numberBuffer.clear()
                            }
                            instructions.add(Turn(1))
                        }

                        'L' -> {
                            if (numberBuffer.isNotEmpty()) {
                                instructions.add(Walk(numberBuffer.joinToString("").toInt()))
                                numberBuffer.clear()
                            }
                            instructions.add(Turn(-1))
                        }

                        else -> numberBuffer.add(c)
                    }
                }
                if (numberBuffer.isNotEmpty()) {
                    instructions.add(Walk(numberBuffer.joinToString("").toInt()))
                    numberBuffer.clear()
                }
            }
        }


        var pos = 0 to grid[0].indexOfFirst { it != ' ' }
        var direction = 0
        instructions.forEach { instruction ->
            if (instruction is Walk) {

                for (i in 0 until instruction.amount) {
                    var next = pos + direction.getStep()
                    var chargeDirection = -1
                    if (grid.get(next) == ' ') {
                        // need to wrap

                        val myGridId = gridSpecs.entries.first { it.value.contains(pos) }.key
                        val wrapper = wrapSpecs.first { w ->
                            w.fromGrid == myGridId && w.direction == direction
                        }
                        val fromGrid = gridSpecs[myGridId]!!
                        val dstGrid = gridSpecs[wrapper.toGrid]!!
                        val relPos = (pos.first - fromGrid.from.first) to (pos.second - fromGrid.from.second)

                        val (wrappedPosRel, newDirection) = wrapper.posMapper(relPos)
                        val wrapPos =
                            (dstGrid.from.first + wrappedPosRel.first) to (dstGrid.from.second + wrappedPosRel.second)

                        if (grid.get(wrapPos) == '#') break
                        next = wrapPos
                        chargeDirection = newDirection
                    }


                    if (grid.get(next) == '#') {
                        // abort
                        break
                    } else {
                        if (chargeDirection > -1) {
                            direction = chargeDirection
                        }
                        grid[next.first][next.second] = direction.toArrow()
                        pos = next
                    }
                }

            } else {
                val t = instruction as Turn
                direction = direction.turn(t)
            }
        }

        return (1000 * (pos.first + 1)) + (4 * (pos.second + 1)) + direction
    }

    data class WrappingSpec(
        val fromGrid: Int,
        val direction: Int,
        val toGrid: Int,
        val posMapper: (fromPoint: Point) -> Pair<Point, Int>
    )


    fun List<CharArray>.get(pos: Point) = this.getOrElse(pos.first) { CharArray(0) }.getOrElse(pos.second) { ' ' }

    fun Int.toArrow() = when (this) {
        0 -> '>'
        1 -> 'v'
        2 -> '<'
        3 -> '^'
        else -> error("")
    }

    fun Int.turn(t: Turn): Int = ((this + 4) + t.amount) % 4

    fun Int.getStep() = when (this) {
        0 -> 0 to 1
        1 -> 1 to 0
        2 -> 0 to -1
        3 -> -1 to 0
        else -> error("")
    }

    data class Area(
        val from: Point,
        val to: Point
    ) {
        fun contains(p: Point) = p.first in from.first..to.first && p.second in from.second..to.second
    }

}

interface Instruction2
data class Walk(val amount: Int) : Instruction2
data class Turn(val amount: Int) : Instruction2
