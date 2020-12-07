package com.dehnes.adventofcode_2020.day02

import java.io.File

fun isValidPasswordPart1(passwordToPolicy: Pair<String, Pair<IntRange, Char>>) = passwordToPolicy.second.first.contains(passwordToPolicy.first
        .filter { it == passwordToPolicy.second.second }
        .count()
)

fun isValidPasswordPart2(passwordToPolicy: Pair<String, Pair<IntRange, Char>>): Boolean {
    val isFirst = passwordToPolicy.first.getOrNull(passwordToPolicy.second.first.first - 1) == passwordToPolicy.second.second
    val isSecond = passwordToPolicy.first.getOrNull(passwordToPolicy.second.first.last - 1) == passwordToPolicy.second.second
    return (isFirst && !isSecond) || (!isFirst && isSecond)
}

val pattern = "(\\d+)-(\\d+)\\s+(\\S):\\s+(\\S+)".toRegex()
val passwords = File("resources/day02.txt").readLines().map {
    pattern.findAll(it).toList().flatMap { matchResult -> matchResult.groupValues }
            .let { parts ->
                parts[4] to (parts[1].toInt()..parts[2].toInt() to parts[3][0])
            }
}

fun main() {
    println("Part1: ${passwords.filter(::isValidPasswordPart1).count()}")
    println("Part1: ${passwords.filter(::isValidPasswordPart2).count()}")
}