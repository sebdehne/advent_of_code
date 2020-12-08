package com.dehnes.adventofcode_2020.day02

import java.io.File

fun isValidPasswordPart1(passwordToPolicy: Pair<String, Pair<IntRange, Char>>) = passwordToPolicy.second.first.contains(passwordToPolicy.first
        .filter { it == passwordToPolicy.second.second }
        .count()
)

fun isValidPasswordPart2(passwordToPolicy: Pair<String, Pair<IntRange, Char>>): Boolean {
    val isFirst = passwordToPolicy.first.getOrNull(passwordToPolicy.second.first.first - 1) == passwordToPolicy.second.second
    val isSecond = passwordToPolicy.first.getOrNull(passwordToPolicy.second.first.last - 1) == passwordToPolicy.second.second
    return isFirst.xor(isSecond)
}

val pattern = "(\\d+)-(\\d+)\\s+(\\S):\\s+(\\S+)".toRegex()
val passwords = File("resources/day02.txt").readLines().map {
    val (start, end, c, password) = pattern.find(it)!!.destructured
    password to (start.toInt()..end.toInt() to c[0])
}

fun main() {
    println("Part1: ${passwords.filter(::isValidPasswordPart1).count()}")
    println("Part1: ${passwords.filter(::isValidPasswordPart2).count()}")
}