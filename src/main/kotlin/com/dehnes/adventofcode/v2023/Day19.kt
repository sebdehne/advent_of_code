package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getText
import com.dehnes.adventofcode.utils.RangeUtils.len
import org.junit.jupiter.api.Test

class Day19 {

    data class Rule(val category: Char, val op: Char, val value: Int, val dst: String)
    data class Workflow(val name: String, val rules: List<Rule>, val dst: String)

    private val ratings: List<Map<Char, Int>>
    private val workflows: Map<String, Workflow>

    init {
        val (workflowsText, parts) = getText().split("\n\n")
        ratings = parts.lines().map { str ->
            str.replace("{", "").replace("}", "").split(",").associate { p ->
                val (k, v) = p.split("=")
                k[0] to v.toInt()
            }
        }

        workflows = workflowsText.lines().map { line ->
            val name = line.split("{")[0]
            val split = line.split("{")[1].replace("}", "").split(",")
            val rules = split.dropLast(1).map {
                Rule(
                    it[0],
                    it[1],
                    it.drop(2).split(":")[0].toInt(),
                    it.drop(2).split(":")[1]
                )
            }
            Workflow(name, rules, split.last())
        }.associateBy { it.name }
    }

    @Test
    fun run() {
        check(ratings.filter { r -> solvePart1("in", r) }.sumOf { it.values.sum() } == 402185)
        check(solvePart2("in", "xmas".associateWith { 1..4000 }) == 130291480568730L)
    }

    private fun solvePart1(destination: String, r: Map<Char, Int>): Boolean = when (destination) {
        "A" -> true
        "R" -> false
        else -> {
            val w = workflows[destination]!!
            solvePart1(
                w.rules.firstOrNull {
                    when (it.op) {
                        '<' -> r[it.category]!! < it.value
                        '>' -> r[it.category]!! > it.value
                        else -> error("")
                    }
                }?.dst ?: w.dst,
                r
            )
        }
    }

    private fun solvePart2(dst: String, input: Map<Char, IntRange?>): Long = when (dst) {
        "A" -> input.values.fold(1L) { acc, r -> acc * r.len() }

        "R" -> 0L

        else -> {
            var combinations = 0L

            val w = workflows[dst]!!
            val todo = input.toMutableMap()

            w.rules.forEach { rule ->
                val target = todo[rule.category]!!

                val (matched, notMatched) = when (rule.op) {
                    '>' -> if (rule.value in target) {
                        ((rule.value + 1)..target.last) to (target.first..rule.value)
                    } else if (rule.value < target.first) {
                        target to null
                    } else {
                        null to target
                    }

                    '<' -> if (rule.value in target) {
                        (target.first..<rule.value) to (rule.value..target.last)
                    } else if (target.last < rule.value) {
                        target to null
                    } else {
                        null to target
                    }

                    else -> error("")
                }

                combinations += if (matched != null) {
                    solvePart2(rule.dst, todo + (rule.category to matched))
                } else 0L

                todo[rule.category] = notMatched
            }

            combinations + solvePart2(w.dst, todo)
        }
    }

}