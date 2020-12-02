package com.dehnes.adventofcode_2020.day01

import java.io.File
import java.nio.charset.Charset

sealed class Result
data class DoneResult(
    val result: List<Int>
) : Result()

object NoResult : Result()

fun findTargetRecursive(target: Int, degreeRemaining: Int, inputDescensingOrder: List<Int>, candidates: List<Int>): Result {

    for (candidate in inputDescensingOrder) {
        if (degreeRemaining > 0) {
            val result = findTargetRecursive(
                target,
                degreeRemaining - 1,
                inputDescensingOrder,
                candidates + candidate
            )
            if (result is DoneResult) {
                return result
            } // continue
        } else {
            val sum = (candidates).sum()
            if (sum == target) {
                return DoneResult(candidates)
            } else if (sum > target) {
                return NoResult
            } // else continue
        }
    }

    return NoResult
}

fun main() {
    val result = findTargetRecursive(
        2020,
        3,
        File("resources/day01.txt").readLines(Charset.defaultCharset())
            .map { it.toInt() }
            .sorted()
            .reversed(),
        emptyList()
    )

    println(result)
    if (result is DoneResult) {
        println("Product: " + result.result.reduce { acc, i -> acc * i })
    }
}
