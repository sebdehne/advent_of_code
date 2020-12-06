package com.dehnes.adventofcode_2020.day06

import java.io.File

fun main() {
    val groups = File("resources/day06.txt").readText().split("\n\n")

    println("Part1: " + groups.map { group ->
        group.replace("\n", "").toSet().size
    }.sum())

    println("Part2: " + groups.map { group ->
        group.split("\n")
                .filter { it.isNotEmpty() }
                .fold(
                        group.replace("\n", "").toSet()
                ) { acc, person ->
            acc.intersect(person.toList())
        }.size
    }.sum())
}