package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day02 {

    val originalCode = getText().split(",").map { it.toInt() }

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
        check(runCode(12, 2) == 2782414)

        var result = 0
        outer@ for (noun in 0..99) {
            for (verb in 0..99) {
                if (runCode(noun, verb) == 19690720) {
                    result = 100 * noun + verb
                    break@outer
                }
            }
        }

        check(result == 9820)


    }

}