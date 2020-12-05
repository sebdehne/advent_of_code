package com.dehnes.adventofcode_2020.day05

import java.io.File
import java.lang.Integer.parseInt
import java.nio.charset.Charset

data class Seat(
        val row: Int,
        val column: Int
) {

    companion object {
        fun decode(code: String) = Seat(
                parseInt(code.substring(0..6).replace("F", "0").replace("B", "1"), 2),
                parseInt(code.substring(7..9).replace("L", "0").replace("R", "1"), 2)
        )
    }

    fun seatId() = row * 8 + column
    operator fun plus(addToId: Int) = Seat(row + ((column + addToId) / 8), (column + addToId) % 8)
}

fun main() {

    val seats = File("resources/day05.txt")
            .readLines(Charset.defaultCharset())
            .map(Seat::decode)
            .sortedBy { it.seatId() }

    println("Part1: highest seat ID: ${seats.maxByOrNull { it.seatId() }?.seatId()}")

    val mySeat = seats.filterIndexed { index, seat ->
        index < seats.size - 2 && seat + 1 != seats[index + 1]
    }.single() + 1

    println("Part2: my seat=$mySeat - ID: ${mySeat.seatId()}")

}