package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day01 {
    val input = getLines().map(String::toInt)

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

        check(countIncrements(1) == 1475)
        check(countIncrements(3) == 1516)
    }
}

fun <T> List<T>.zipBy(windowSize: Int) = this.mapIndexedNotNull { index, i ->
    if (index + windowSize > this.size) {
        null
    } else {
        this.subList(index, index + windowSize)
    }
}