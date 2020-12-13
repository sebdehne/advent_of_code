package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day09 {

    val numbers = File("resources/day09.txt").readLines().map { it.toLong() }
    val preamble = 25

    fun part1() = numbers.filterIndexed { i, l ->
        i >= preamble && numbers.subList(i - preamble, i).let { subList ->
            subList.none { n1 -> subList.any { n2 -> n1 + n2 == l } }
        }
    }.first()

    fun part2(): Long {
        var start = 0
        var end = 0
        val target = part1()
        while (true) {
            val candidateSet = numbers.subList(start, end)
            val sum = candidateSet.sum()
            when {
                sum < target -> end++
                sum > target -> end = ++start
                else -> return candidateSet.minOrNull()!! + candidateSet.maxOrNull()!!
            }
        }
    }

    @Test
    fun run() {
        println(part1()) // 552655238
        println(part2()) // 70672245
    }
}