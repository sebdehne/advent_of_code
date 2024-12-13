package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day04 {
    val passports = File("resources/2020/day04.txt").readText().split("\n\n").map {
        it.replace("\n", " ").split(" ").associate {
            val (left, right) = it.split(":")
            left to right
        }
    }

    val validationRulesPart1 = mapOf(
            "byr" to { value: String? -> value != null },
            "iyr" to { value: String? -> value != null },
            "eyr" to { value: String? -> value != null },
            "hgt" to { value: String? -> value != null },
            "hcl" to { value: String? -> value != null },
            "ecl" to { value: String? -> value != null },
            "pid" to { value: String? -> value != null },
            "cid" to { true }
    )

    val validationRulesPart2 = mapOf(
            "byr" to { value: String? ->
                if (value?.matches("\\d\\d\\d\\d".toRegex()) == true) {
                    value.toInt() in 1920..2002
                } else false
            },
            "iyr" to { value: String? ->
                if (value?.matches("\\d\\d\\d\\d".toRegex()) == true) {
                    value.toInt() in 2010..2020
                } else false
            },
            "eyr" to { value: String? ->
                if (value?.matches("\\d\\d\\d\\d".toRegex()) == true) {
                    value.toInt() in 2020..2030
                } else false
            },
            "hgt" to { value: String? ->
                if (value?.matches("\\d+cm".toRegex()) == true) {
                    value.replace("cm", "").toInt() in 150..193
                } else if (value?.matches("\\d+in".toRegex()) == true) {
                    value.replace("in", "").toInt() in 59..76
                } else false
            },
            "hcl" to { value: String? -> value?.matches("#[0-9a-f]{6}".toRegex()) == true },
            "ecl" to { value: String? -> value in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") },
            "pid" to { value: String? -> value?.matches("[0-9]{9}".toRegex()) == true },
            "cid" to { true }
    )

    fun countValidPassports(rules: Map<String, (value: String?) -> Boolean>) = passports.count { p ->
        rules.filterNot { e -> e.value.invoke(p[e.key]) }.isEmpty()
    }

    @Test
    fun run() {
        println("Part1: ${countValidPassports(validationRulesPart1)}") // 230
        println("Part2: ${countValidPassports(validationRulesPart2)}") // 156
    }
}