package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day02 {
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
    val passwords = File("resources/2020/day02.txt").readLines().map {
        val (start, end, c, password) = pattern.find(it)!!.destructured
        password to (start.toInt()..end.toInt() to c[0])
    }

    @Test
    fun run() {
        println("Part1: ${passwords.filter(::isValidPasswordPart1).count()}")
        println("Part1: ${passwords.filter(::isValidPasswordPart2).count()}")
    }
}