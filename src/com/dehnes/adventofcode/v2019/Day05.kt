package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day05 {

    val originalCode = getText().split(",").map { it.toLong() }

    @Test
    fun main() {
        var result = 0L
        intcodeComputer(originalCode, { 1 }) { result = it }
        check(result == 5074395L)

        intcodeComputer(originalCode, { 5 }) { result = it }
        check(result == 8346937L)
    }
}