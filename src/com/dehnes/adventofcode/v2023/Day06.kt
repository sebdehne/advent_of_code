package com.dehnes.adventofcode.v2023

import org.junit.jupiter.api.Test
import java.io.File

class Day06 {

    @Test
    fun part1() {
        val data = File("resources/2023/day06.txt").readLines()
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
        val data = File("resources/2023/day06.txt").readLines()
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