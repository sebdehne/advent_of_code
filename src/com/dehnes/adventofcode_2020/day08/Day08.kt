package com.dehnes.adventofcode_2020.day08

import java.io.File

val code = File("resources/day08.txt").readLines().map { line ->
    val (op, arg) = line.split(" ")
    op to arg.toInt()
}

fun List<Pair<String, Int>>.testCode(): Pair<Boolean, Long> {
    val linesVisited = mutableSetOf<Int>()
    var nextLineToBeExecuted = 0
    var acc = 0L
    while (true) {
        if (nextLineToBeExecuted >= this.size) {
            return true to acc
        }
        if (nextLineToBeExecuted in linesVisited) {
            return false to acc
        }
        linesVisited.add(nextLineToBeExecuted)
        val nextCmd = this[nextLineToBeExecuted]
        nextLineToBeExecuted += when (nextCmd.first) {
            "jmp" -> nextCmd.second
            "acc" -> {
                acc += nextCmd.second
                null
            }
            else -> null
        } ?: 1
    }
}

fun main() {
    println("Part1: ${code.testCode().second}") // 1797

    listOf("jmp" to "nop", "nop" to "jmp").forEach { replacement ->
        code.indices.forEach { replaceAtIndex ->
            val (terminated, acc) = code.replaceLine(replaceAtIndex, replacement).testCode()
            if (terminated) {
                println("Part2: replace ${replacement.first} with ${replacement.second} at line $replaceAtIndex and you get acc=$acc")
            } // 1036
        }
    }
}

fun List<Pair<String, Int>>.replaceLine(replaceAtIndex: Int, replacement: Pair<String, String>) = this.mapIndexed { index, line ->
    if (replaceAtIndex == index && line.first == replacement.first) {
        replacement.second to line.second
    } else {
        line
    }
}