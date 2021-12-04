package com.dehnes.adventofcode.v2019

import org.junit.jupiter.api.Test

class Day02 {

    val originalCode = inputText(2).split(",").map { it.toInt() }

    fun runCode(noun: Int, verb: Int): Int {
        val code = originalCode.toMutableList()
        code[1] = noun
        code[2] = verb
        var index = 0
        while (true) {
            when (code[index]) {
                1 -> code[code[index + 3]] = code[code[index + 1]] + code[code[index + 2]]
                2 -> code[code[index + 3]] = code[code[index + 1]] * code[code[index + 2]]
                99 -> break
            }
            index += 4
        }
        return code[0]
    }

    @Test
    fun main() {
        println(runCode(12, 2)) // 2782414

        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                if (runCode(noun, verb) == 19690720) {
                    println(100 * noun + verb) // 9820
                }
            }
        }

    }

}