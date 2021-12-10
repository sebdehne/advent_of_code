package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

class Day10 {

    val input = File("resources/2021/day10.txt").readLines()

    val pairs = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )
    val score = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    @Test
    fun run() {
        val completedLines = mutableListOf<List<Char>>()

        val scoreIncomplete = input.sumOf { line ->
            val stack = LinkedList<Char>()
            var illegalChar: Char? = null
            line.forEach { c ->
                if (c in pairs) {
                    stack.push(c)
                } else {
                    if (c != pairs[stack.pop()]!!) {
                        illegalChar = c
                        return@forEach
                    }
                }
            }
            if (illegalChar == null) {
                completedLines.add(stack.map { pairs[it]!! })
                0
            } else {
                score[illegalChar]!!
            }
        }

        val completedLinesScores = completedLines.map { line ->
            line.fold(0L) { acc, c ->
                (acc * 5) + when (c) {
                    ')' -> 1
                    ']' -> 2
                    '}' -> 3
                    '>' -> 4
                    else -> error("")
                }
            }
        }.sorted()

        val scoreCompleted = completedLinesScores[completedLinesScores.size / 2]

        assertEquals(415953, scoreIncomplete)
        assertEquals(2292863731, scoreCompleted)
    }
}