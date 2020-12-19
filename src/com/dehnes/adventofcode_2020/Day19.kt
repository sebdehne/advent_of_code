package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day19 {

    val messages: String
    val unresolvedRules: List<Pair<Int, String>>
    val messageList: List<String>

    init {
        val (a, b) = File("resources/day19.txt").readText().split("\n\n")
        messages = b
        unresolvedRules = a.split("\n").map { rule ->
            val (id, rest) = rule.split(": ")
            id.toInt() to rest
        }
        messageList = messages.split("\n")
    }

    @Test
    fun part1() {
        val countPart1 = messageList.count { msg ->
            val m = matches(unresolvedRules, unresolvedRules.first { it.first == 0 }, msg)
            m.first.first && m.first.second && msg.length == m.second
        }

        val updatedUnresolvedRules = unresolvedRules.map {
            when (it.first) {
                8 -> { it.first to "42 | 42 8" }
                11 -> { it.first to "42 31 | 42 11 31" }
                else -> it
            }
        }

        val countPart2 = messageList.count { msg ->
            val (matchedAndCompletedSet, endIndex) = matches(updatedUnresolvedRules, updatedUnresolvedRules.first { it.first == 0 }, msg)
            matchedAndCompletedSet.first && matchedAndCompletedSet.second && msg.length == endIndex
        }

        println(countPart1) // 265
        println(countPart2) // 394
    }

    fun matches(unresolvedRules: List<Pair<Int, String>>, r: Pair<Int, String>, input: String, inputIndex: Int = 0): Pair<Pair<Boolean, Boolean>, Int> {
        val (ruleId, rule) = r
        val alternatives = rule.split(" | ")
        return if (alternatives.size > 1) alternatives.fold((false to false) to 0) { acc, alt ->
            if (!(acc.first.first)) {
                matches(unresolvedRules, ruleId to alt, input, inputIndex)
            } else
                acc
        } else {
            var index = inputIndex
            var completedSet = true
            alternatives[0].split(" ").forEach { nextElement ->
                if (index == input.length) {
                    completedSet = completedSet and r.second.contains(ruleId.toString())
                    return@forEach
                }

                if (nextElement.startsWith("\"")) {
                    if (input[index] != nextElement[1]) {
                        return (false to false) to 0
                    }
                    index++
                } else {
                    val (matchedAndCompletedSet, endIndex) = matches(
                            unresolvedRules,
                            unresolvedRules.first { it.first == nextElement.toInt() },
                            input,
                            index
                    )

                    if (!matchedAndCompletedSet.first) {
                        return (false to false) to 0
                    }
                    index = endIndex
                    completedSet = completedSet and matchedAndCompletedSet.second
                }
            }
            return (true to completedSet) to index
        }
    }
}