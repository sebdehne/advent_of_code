package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File

class Day01 {
    val input = File("resources/2021/day01.txt").readLines().map(String::toInt)

    @Test
    fun run() {
        val countIncrements = { windowsSize: Int ->
            input.zipBy(windowsSize).zipWithNext().fold(0) { acc, list ->
                if (list.second.sum() > list.first.sum()) {
                    acc + 1
                } else {
                    acc
                }
            }
        }

        println(countIncrements(1))
        println(countIncrements(3))
    }
}

fun <T> List<T>.zipBy(windowSize: Int) = this.mapIndexedNotNull { index, i ->
    if (index + windowSize > this.size) {
        null
    } else {
        this.subList(index, index + windowSize)
    }
}