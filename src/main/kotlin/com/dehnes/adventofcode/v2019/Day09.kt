package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day09 {
    val originalCode = getText().split(",").map { it.toLong() }

    @Test
    fun main() {
        check((intcodeComputer_(State.init(originalCode), 1) as Output).value == 2789104029L)
        check((intcodeComputer_(State.init(originalCode), 2) as Output).value == 32869L)
    }
}