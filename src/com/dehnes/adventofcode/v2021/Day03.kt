package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File

class Day03 {

    val input = File("resources/2021/day03.txt").readLines().map {
        it.toList().map { it.toString().toInt() }
    }

    @Test
    fun run() {
        val size = input[0].size
        val gamma = IntArray(size)
        val epsilon = IntArray(size)

        (0 until size).forEach { column ->
            val (zeros, onces) = input.map { it[column] }.partition { it == 0 }
            gamma[column] = if (zeros.size > onces.size) 0 else 1
            epsilon[column] = if (zeros.size < onces.size) 0 else 1
        }

        val g = gamma.joinToString(separator = "").toInt(2)
        val e = epsilon.joinToString(separator = "").toInt(2)
        println(g * e) // 3923414
    }


    @Test
    fun run2() {
        val findCommon = { column: List<Int>, most: Boolean ->
            val (zeros, onces) = column.partition { it == 0 }
            if (most) {
                if (zeros.size > onces.size) 0 else if (onces.size > zeros.size) 1 else 1
            } else {
                if (zeros.size < onces.size) 0 else if (onces.size < zeros.size) 1 else 0
            }
        }

        val calc = { input: List<List<Int>>, most: Boolean ->
            var list = input
            var column = 0
            while (list.size > 1) {
                val r = findCommon(list.map { it[column] }, most)
                list = list.filter { it[column] == r }
                column++
            }
            list[0].joinToString(separator = "").toInt(2)
        }

        val oxygen = calc(input, true)
        val co2 = calc(input, false)
        println(oxygen * co2) // 5852595
    }
}
