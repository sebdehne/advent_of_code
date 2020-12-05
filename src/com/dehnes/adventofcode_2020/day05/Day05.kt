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
}

fun main() {
    val seats = File("resources/day05.txt")
            .readLines(Charset.defaultCharset())
            .map(Seat::decode)
            .sortedBy { it.seatId() }

    println("Part1: highest seat ID: ${seats.maxByOrNull { it.seatId() }?.seatId()}")

    val rowWithMissingSeat = seats
            .groupBy { it.row }
            .filterNot { it.key == seats.first().row || it.key == seats.last().row }
            .filter { e -> e.value.size < 8 }
            .entries.first()
    val missingSeat = Seat(
            rowWithMissingSeat.key,
            (0..7).filterNot { it in rowWithMissingSeat.value.map { it.column } }.single())

    println("Part2: My seat is: $missingSeat - ID: ${missingSeat.seatId()}")

}