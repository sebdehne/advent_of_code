package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.Point
import org.junit.jupiter.api.Test

class Day13 {
    val originalCode = inputText(13).split(",").map { it.toLong() }.toMutableList()

    @Test
    fun part1() {
        val state = GameState()
        val l = mutableListOf<Long>()
        intcodeComputer(originalCode, { 0 }) {
            l.add(it)

            if (l.size == 3) {
                state.update(l)
                l.clear()
            }
        }

        check(state.countBlocks() == 309) // part1
        state.draw()
    }

    @Test
    fun part2() {
        val state = GameState()
        val l = mutableListOf<Long>()
        val drawFrequency = 100000L // set to 1 to watch the game live
        var steps = 0L

        originalCode[0] = 2
        intcodeComputer(originalCode, { state.calcJoystick() }) {
            l.add(it)

            if (l.size == 3) {
                if (state.update(l)) {
                    if ((steps++ % drawFrequency) == 0L) {
                        state.draw()
                        Thread.sleep(250)
                    }
                }
                l.clear()
            }
        }

        println("End score: ${state.score}")
        check(state.score == 15410L)
    }


    companion object {
        class GameState(
            var score: Long = 0,
            val display: Array<CharArray> = Array(25) { CharArray(35) { ' ' } },
            var ballPos: Point = -2 to -2, // x to y
            var paddlePos: Point = 0 to 0,
        ) {

            fun calcJoystick(): Long {
                val deltaX = paddlePos.first - ballPos.first

                return if (deltaX > 0) -1 else if (deltaX < 0) 1 else 0
            }

            fun update(input: List<Long>): Boolean {
                val one = input[0].toInt()
                val two = input[1].toInt()
                val three = input[2]

                return if (one == -1 && two == 0) {
                    if (three >= score) {
                        score = three
                    } else {
                        error("decreasing score to $three")
                    }
                    false
                } else {
                    val type = TileType.entries.first { it.value == three.toInt() }
                    display[two][one] = when (type) {
                        TileType.wall -> '#'
                        TileType.block -> '.'
                        TileType.ball -> '*'
                        TileType.paddle -> '-'
                        TileType.empty -> ' '
                    }

                    when (type) {
                        TileType.paddle -> {
                            paddlePos = one to two
                            false
                        }

                        TileType.ball -> {
                            ballPos = one to two
                            true
                        }

                        else -> false
                    }
                }
            }

            fun countBlocks() = display.sumOf { it.count { it == '.' } }

            fun draw() {
                println("Score: $score")
                display.forEach { line ->
                    println(line.joinToString("") { it.toString() })
                }
            }
        }

        enum class TileType(val value: Int) {
            empty(0),
            wall(1),
            block(2),
            paddle(3),
            ball(4),
        }


    }
}