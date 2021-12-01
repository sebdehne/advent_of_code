package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day18 {

    val expressions = File("resources/2020/day18.txt").readLines()

    @Test
    fun test() {
        println(expressions.map { line -> calc(line).second }.sum())
        println(expressions.map { line -> calcAdditionFirst(line) }.sum())
    }

    fun calcAdditionFirst(expr: String): Long {
        var transformed = expr
        while (transformed.contains("(")) {
            val openIndex = transformed.indexOf("(")
            val endIndex = findClosingIndex(transformed, openIndex)
            val value = calcAdditionFirst(transformed.substring(openIndex + 1, endIndex))
            transformed = transformed.replaceRange(openIndex, endIndex + 1, value.toString())
        }

        while (transformed.contains("+")) {
            val i = transformed.indexOf("+")
            val leftPartStr = transformed.substring(0, i - 1)
            val leftPartLength = leftPartStr.reversed().indexOf(" ").let {
                if (it == -1) {
                    leftPartStr.length
                } else {
                    it
                }
            }
            val rightPartStr = transformed.substring(i + 2)
            val rightPartLength = rightPartStr.indexOf(" ").let {
                if (it == -1) {
                    rightPartStr.length
                } else {
                    it
                }
            }
            val left = transformed.substring(i - leftPartLength - 1, i - 1)
            val right = transformed.substring(i + 2, i + 2 + rightPartLength)

            transformed = transformed.replaceRange(
                    i - 1 - leftPartLength,
                    i + 2 + rightPartLength,
                    (left.toLong() + right.toLong()).toString()
            )
        }

        return transformed.split(" * ").map { it.toLong() }.reduce { acc, l -> acc * l }
    }

    fun calc(expr: String): Pair<Int, Long> {
        var (endIndex, value) = nextValue(expr, this::calc)
        while (endIndex + 1 < expr.length) {
            val op = expr[endIndex + 2]
            val (rightEndIndex, right) = nextValue(expr.substring(endIndex + 4), this::calc)
            value = if (op == '+') value + right else value * right
            endIndex += rightEndIndex + 4
        }
        return endIndex to value
    }

    fun nextValue(expr: String, calcFn: (e: String) -> Pair<Int, Long>): Pair<Int, Long> = if (expr[0] == '(') {
        val endIndex = findClosingIndex(expr, 0)
        endIndex to calcFn(expr.substring(1, endIndex)).second
    } else {
        val index = expr.indexOf(" ")
        if (index == -1) {
            expr.length - 1 to expr.toLong()
        } else {
            index - 1 to expr.substring(0, index).toLong()
        }
    }

    fun findClosingIndex(expr: String, startIdx: Int): Int {
        var openCount = 1
        expr.forEachIndexed { index, c ->
            when {
                index <= startIdx -> {
                }
                c == '(' -> openCount++
                c == ')' -> if (--openCount == 0) return index
            }
        }
        error("")
    }
}