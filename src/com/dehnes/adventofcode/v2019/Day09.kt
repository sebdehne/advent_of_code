package com.dehnes.adventofcode.v2019

import org.junit.jupiter.api.Test

class Day09 {
    val originalCode = inputText(9).split(",").map { it.toLong() }

    @Test
    fun main() {
        intcodeComputer(originalCode, { 1 }, ::println) // 2789104029
        intcodeComputer(originalCode, { 2 }, ::println) // 32869
    }
}