package com.dehnes.adventofcode_2020.day04

import java.io.File
import java.nio.charset.Charset

val validationRulesPart1 = mapOf(
        "byr" to { value: String? -> value != null },
        "iyr" to { value: String? -> value != null },
        "eyr" to { value: String? -> value != null },
        "hgt" to { value: String? -> value != null },
        "hcl" to { value: String? -> value != null },
        "ecl" to { value: String? -> value != null },
        "pid" to { value: String? -> value != null },
        "cid" to { value: String? -> true }
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
        "cid" to { value: String? -> true }
)

fun main() {

    val passports = readAllPassports()

    val countValidPassports = { validationRules: Map<String, (value: String?) -> Boolean> ->
        passports.count { p ->
            validationRules.filter { validationRule ->
                val value = p[validationRule.key]
                !validationRule.value.invoke(value)
            }.isEmpty()
        }
    }

    val countPart1 = countValidPassports(validationRulesPart1)
    val countPart2 = countValidPassports(validationRulesPart2)

    println("Done: part1=$countPart1, part2=$countPart2, totalPassports=${passports.size}") // 230, 156, 255
}

private fun readAllPassports() = File("resources/day04.txt").readText(Charset.defaultCharset()).split("\n\n").map {
    it.replace("\n", " ").split(" ").associate {
        val (left, right) = it.split(":")
        left to right
    }
}