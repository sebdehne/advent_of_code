package com.dehnes.adventofcode.v2019

import org.junit.jupiter.api.Test

class Day16 {

    val signal = inputText(16).toList().map { it.toString().toInt() }
    val basePattern = listOf(0, 1, 0, -1)

    @Test
    fun test() {
        // part1
        check(calc100Phases(signal).subList(0, 8).joinToString(separator = "") == "53296082")
    }

    @Test
    fun test2() {
        val part2Input = (1..10).flatMap { signal }

        val result = calc100Phases(part2Input)

        val map = mutableMapOf<String, MutableList<Int>>()

        result.indices.toList().dropLast(7).forEach { i ->
            val pattern = result.subList(i, i + 8).joinToString("")
            val positions = map.getOrPut(pattern) { mutableListOf() }
            positions.add(i)
        }

        println()
    }

    private fun calc100Phases(signal: List<Int>): List<Int> {
        var phaseResult = signal
        (1..100).forEach { p ->
            phaseResult = calcPhase(phaseResult)
        }
        return phaseResult
    }

    private fun calcPhase(input: List<Int>) = List(input.size) { index ->
        input.mapIndexed { index2, digit -> digit * calcMultiplyer(index2, index) }.sum()
    }.map { it.toString().last().toString().toInt() }

    private fun calcMultiplyer(patternPosition: Int, inputDigitIndex: Int): Int {
        val n1Final = patternPosition + 1 // offset to left by one
        val n2Final = inputDigitIndex + 1

        val patternSize = basePattern.size * n2Final
        val n = n1Final.mod(patternSize)
        return basePattern[(n * basePattern.size) / patternSize]
    }
}