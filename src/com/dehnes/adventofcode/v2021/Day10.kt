package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*

class Day10 {

    val input = getLines()

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

        check(scoreIncomplete == 415953)
        check(scoreCompleted == 2292863731)
    }
}