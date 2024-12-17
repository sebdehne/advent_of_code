package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test
import java.util.*

class Day17 {

    @Test
    fun run() {
        val (regs, instructionsStr) = getText().split("\n\n")
        var (a, b, c) = regs.lines().map { it.split(": ")[1] }.map { it.toLong() }
        val instructions = instructionsStr.split(": ")[1].split(",").map { it.toInt() }

        check("2,3,4,7,5,7,3,0,7" == run(instructions, a, b, c).joinToString(","))

        var part2 = 0L
        val todo = LinkedList<Pair<Int, Long>>()
        todo.add(instructions.size - 1 to 0L)
        while (todo.isNotEmpty()) {
            val (pos, aBase) = todo.remove()
            for (a in (aBase * 8..<(aBase * 8 + 8))) {
                if (instructions.drop(pos) == run(instructions, a, 0, 0) && part2 == 0L) {
                    if (pos == 0) {
                        part2 = a
                        break
                    }
                    todo.add(pos - 1 to a)
                }
            }
        }

        check(190384609508367L == part2)
    }

    private fun run(instructions: List<Int>, aIn: Long, bIn: Long, cIn: Long): List<Int> {
        var a = aIn
        var b = bIn
        var c = cIn

        fun combo(operand: Int): Long = when (operand) {
            in 0..3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> error("")
        }

        var pointer = 0
        val output = mutableListOf<Int>()
        while (pointer < instructions.size) {
            val operand = instructions[pointer + 1]

            when (instructions[pointer]) {
                0 -> a = a / 1.shl(combo(operand).toInt())
                1 -> b = b xor operand.toLong()
                2 -> b = combo(operand) % 8
                3 -> pointer = (if (a == 0L) pointer else operand - 2)
                4 -> b = b xor c
                5 -> output.add((combo(operand) % 8).toInt())
                6 -> b = a / 1.shl(combo(operand).toInt())
                7 -> c = a / 1.shl(combo(operand).toInt())
            }
            pointer += 2
        }

        return output
    }
}