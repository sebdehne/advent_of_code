package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day16 {

    @Test
    fun test() {
        val (rangesStr, myTicketStr, otherTicketsStr) = File("resources/day16.txt").readText().split("your ticket:", "nearby tickets:")
        val validRanges = rangesStr.trim().split("\n").map { range ->
            val (name, r1From, r1To, r2From, r2To) = "([\\w\\s]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)".toRegex().find(range)!!.destructured
            name to ((r1From.toInt()..r1To.toInt()) to (r2From.toInt()..r2To.toInt()))
        }
        val myTicket = myTicketStr.trim().split(",").map { it.toInt() }
        val otherTickets = otherTicketsStr.trim().split("\n").map { ticket ->
            ticket.split(",").map { it.toInt() }
        }

        println("Part1: " + otherTickets.flatten().filter { value -> validRanges.none { it.fits(value) } }.sum()) // 25916

        val validOtherTickets = otherTickets.filter { t -> t.all { value -> validRanges.any { it.fits(value) } } }

        val candidates = validOtherTickets[0].indices.map { it to validRanges }.toMap().toMutableMap()
        validOtherTickets.forEach { ticket ->
            ticket.forEachIndexed { index, value ->
                candidates[index] = candidates[index]!!.filter { it.fits(value) }
            }
        }

        val fieldsIdentified = candidates.toList().sortedBy { it.second.size }.fold(emptyMap<Int, String>()) { acc, pair ->
            acc + (pair.first to pair.second.first { r -> r.first !in acc.values }.first)
        }

        val part2 = fieldsIdentified
                .filter { fieldAndRange -> fieldAndRange.value.startsWith("departure") }
                .map { myTicket[it.key].toLong() }
                .reduce { acc, i -> acc * i }

        println("Part2: $part2") // 2564529489989
    }
}

fun Pair<String, Pair<IntRange, IntRange>>.fits(i: Int) = i in this.second.first || i in this.second.second