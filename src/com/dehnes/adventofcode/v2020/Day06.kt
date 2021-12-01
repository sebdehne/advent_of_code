package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day06 {

    val groups = File("resources/2020/day06.txt").readText().split("\n\n")

    @Test
    fun run() {
        println("Part1: " + groups.map { group -> group.replace("\n", "").toSet().size }.sum())

        println("Part2: " + groups.map { group ->
            group.split("\n")
                    .filter { it.isNotEmpty() }
                    .fold(group.replace("\n", "").toSet()) { acc, person ->
                        acc.intersect(person.toList())
                    }.size
        }.sum())
    }

}