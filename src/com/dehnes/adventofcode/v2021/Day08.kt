package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day08 {
    val data = File("resources/2021/day08.txt").readLines()
        .map { it.split("|").let { it[0].trim().split(" ") to it[1].trim().split(" ") } }

    @Test
    fun part1() {
        assertEquals(
            452,
            data
                .map { it.second }
                .sumOf { it.count { it.length in listOf(2, 3, 4, 7) } }
        )
    }

    @Test
    fun part2() {
        val total = data.sumOf { line ->
            val allDigits = line.first.map { it.toList() }

            val one = allDigits.single { it.size == 2 }
            val seven = allDigits.single { it.size == 3 }
            val eight = allDigits.single { it.size == 7 }
            val four = allDigits.single { it.size == 4 }

            val aSegment = seven.minus(one).single()

            val gSegment = allDigits.filter { it.size == 6 }
                .map { it.minus(four) }
                .single { it.size == 2 && aSegment in it }
                .minus(aSegment)
                .single()

            val dSegment = allDigits.filter { it.size == 5 }
                .map { it.minus(one) }
                .single { it.size == 3 }
                .minus(aSegment)
                .minus(gSegment)
                .single()

            val bSegment = four.minus(one).minus(dSegment).single()

            val fSegment = allDigits.filter { it.size == 5 }
                .map { it.minus(aSegment).minus(bSegment).minus(dSegment).minus(gSegment) }
                .single { it.size == 1 }
                .single()

            val cSegment = one.minus(fSegment).single()

            val eSegment = eight
                .minus(four)
                .minus(aSegment)
                .minus(cSegment)
                .minus(gSegment)
                .single()

            line.second.map { d ->
                when {
                    d.length == 2 -> 1
                    d.length == 3 -> 7
                    d.length == 4 -> 4
                    d.length == 5 && bSegment in d -> 5
                    d.length == 5 && fSegment in d -> 3
                    d.length == 5 -> 2
                    d.length == 6 && eSegment !in d -> 9
                    d.length == 6 && cSegment !in d -> 6
                    d.length == 6 -> 0
                    d.length == 7 -> 8
                    else -> error("")
                }
            }.joinToString(separator = "").toLong()
        }

        assertEquals(1096964, total)
    }

    fun List<Char>.minus(other: List<Char>) = this.filterNot { it in other }
    fun List<Char>.minus(other: Char) = this.filterNot { it == other }

}