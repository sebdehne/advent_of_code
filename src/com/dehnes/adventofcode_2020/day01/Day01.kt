package com.dehnes.adventofcode_2020.day01

import java.io.File
import java.nio.charset.Charset

sealed class Result
data class DoneResult(
        val result: List<Int>
) : Result()

object NoResult : Result()

fun findTargetRecursive(target: Int, degreeRemaining: Int, inputDescendingOrder: List<Int>, candidates: List<Int>): Result {
    inputDescendingOrder.forEach { candidate ->
        if (degreeRemaining > 0) {
            findTargetRecursive(
                    target,
                    degreeRemaining - 1,
                    inputDescendingOrder,
                    candidates + candidate
            ).also { result ->
                if (result is DoneResult) {
                    return result
                }
            }
        } else {
            candidates.sum().also { sum ->
                if (sum == target) {
                    return DoneResult(candidates)
                } else if (sum > target) {
                    return NoResult
                }
            }
        }
    }

    return NoResult
}

fun main() {
    for (degree in 2..3) {
        val result = findTargetRecursive(
                2020,
                degree,
                File("resources/day01.txt").readLines(Charset.defaultCharset())
                        .map { it.toInt() }
                        .sorted()
                        .reversed(),
                emptyList()
        )
        println("Done: degree=$degree, result=$result, product=" + (result as DoneResult).result.reduce { acc, i -> acc * i })
    }
}
