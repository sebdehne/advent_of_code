package com.dehnes.adventofcode_2020.day02

import java.io.File
import java.nio.charset.Charset

data class PasswordPolicy(
        val start: Int,
        val end: Int,
        val char: Char
) {
    companion object {
        fun create(policyString: String): PasswordPolicy {
            val parts = policyString.split(" ").map(String::trim)
            val range = parts[0]
            val character = parts[1][0]

            val rangeParts = range.split("-").map(String::trim)
            val start = rangeParts[0].toInt()
            val end = rangeParts[1].toInt()

            return PasswordPolicy(
                    start,
                    end,
                    character
            )
        }
    }
}

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
    val passwords = File("resources/day02.txt")
            .readLines(Charset.defaultCharset())
            .map { line ->
                val map = line.split(":").map(String::trim)
                PasswordPolicy.create(map[0]) to map[1]
            }

    val part1 = passwords
            .filter { it.second.isValidPasswordPart1(it.first) }
            .count()
    println("Result part1: $part1")

    val part2 = passwords
            .filter { it.second.isValidPasswordPart2(it.first) }
            .onEach { println(it) }
            .count()
    println("Result part2: $part2")
}