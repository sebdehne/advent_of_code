package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day06 {

    @Test
    fun part1() {
        val data = getLines()
            .dropLast(1)
            .map { it.split(":")[1].trim() }
            .map {
                it.split(" ")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
            }

        val total = List(data[0].size) { index ->
            data[0][index] to data[1][index]
        }
            .map { bruteForce(it.first.toLong(), it.second.toLong()) }
            .reduce { acc, i -> acc * i }

        check(total == 625968)
    }

    @Test
    fun part2() {
        val data = getLines()
            .dropLast(1)
            .map { it.split(":")[1].trim() }
            .map {
                it.split(" ")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .joinToString("")
                    .toLong()
            }

        check(bruteForce(data[0], data[1]) == 43663323)
    }

    private fun bruteForce(time: Long, record: Long): Int {
        var waysToWin = 0

        var holdTime = 0L
        while (holdTime++ < time) {
            if (holdTime * (time - holdTime) > record) {
                waysToWin++
            }
        }

        return waysToWin
    }
}