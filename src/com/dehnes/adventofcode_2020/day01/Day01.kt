package com.dehnes.adventofcode_2020.day01

import java.io.File

val inputDescendingOrder = File("resources/day01.txt").readLines().map(String::toInt).sorted().reversed()

fun findTargetRecursive(target: Int, degreeRemaining: Int, candidates: List<Int>): List<Int>? {
    for (candidate in inputDescendingOrder) {
        if (degreeRemaining > 0) {
            findTargetRecursive(target, degreeRemaining - 1, candidates + candidate).also { result ->
                if (result != null) {
                    return result
                }
            }
        } else {
            candidates.sum().also { sum ->
                if (sum == target) {
                    return candidates
                } else if (sum > target) {
                    return null
                }
            }
        }
    }
    return null
}

fun List<Int>.product() = this.reduce { acc, i -> acc * i }

fun main() {
    println("Part1: ${findTargetRecursive(2020, 2, emptyList())!!.product()}")
    println("Part1: ${findTargetRecursive(2020, 3, emptyList())!!.product()}")
}
