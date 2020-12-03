package com.dehnes.adventofcode_2020.day02

import java.io.File
import java.nio.charset.Charset

data class PasswordPolicy(
        val start: Int,
        val end: Int,
        val char: Char
)

fun String.isValidPasswordPart1(policy: PasswordPolicy) = IntRange(policy.start, policy.end).contains(this
        .filter { it == policy.char }
        .count()
)

fun String.isValidPasswordPart2(policy: PasswordPolicy): Boolean {
    val isFirst = this.getOrNull(policy.start - 1) == policy.char
    val isSecond = this.getOrNull(policy.end - 1) == policy.char
    return (isFirst && !isSecond) || (!isFirst && isSecond)
}

fun main() {
    val pattern = "(\\d+)-(\\d+)\\s+(\\S):\\s+(\\S+)".toRegex()
    val passwords = File("resources/day02.txt")
            .readLines(Charset.defaultCharset())
            .map { line ->
                pattern.findAll(line)
                        .toList()
                        .flatMap { matchResult -> matchResult.groupValues }
                        .let { parts ->
                            PasswordPolicy(
                                    parts[1].toInt(),
                                    parts[2].toInt(),
                                    parts[3][0]
                            ) to parts[4]
                        }
            }

    val part1 = passwords
            .filter { it.second.isValidPasswordPart1(it.first) }
            .count()
    println("Result part1: $part1")

    val part2 = passwords
            .filter { it.second.isValidPasswordPart2(it.first) }
            .count()
    println("Result part2: $part2")
}