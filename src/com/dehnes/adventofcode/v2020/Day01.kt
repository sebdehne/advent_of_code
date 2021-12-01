package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day01 {

    val inputDescendingOrder = File("resources/2020/day01.txt").readLines().map(String::toInt).sorted().reversed()

    fun findTargetRecursive(target: Int, degreeRemaining: Int, candidates: List<Int> = emptyList()): List<Int>? {
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

    @Test
    fun run() {
        println("Part1: ${findTargetRecursive(2020, 2)!!.product()}")
        println("Part1: ${findTargetRecursive(2020, 3)!!.product()}")
    }

}