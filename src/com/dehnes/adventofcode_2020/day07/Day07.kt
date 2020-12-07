package com.dehnes.adventofcode_2020.day07

import java.io.File

val bagRules = File("resources/day07.txt").readLines().map { ruleStr ->
    ruleStr.split(" ", ",", ".").filter(String::isNotBlank).let { split ->
        "${split[0]} ${split[1]}" to (4 until split.size step 4).map {
            split[it].replace("no", "0").toInt() to "${split[it + 1]} ${split[it + 2]}"
        }.filter { p -> p.first > 0 }
    }
}.toMap()

fun uniqueParents(rules: Map<String, List<Pair<Int, String>>>, color: String): Set<String> = rules
        .filter { other -> other.value.any { it.second == color } }.map { parent -> uniqueParents(rules, parent.key) }
        .flatten()
        .toSet() + color

fun childrenInstances(rules: Map<String, List<Pair<Int, String>>>, color: String): Long =
        1 + rules[color]!!.map { child -> child.first * childrenInstances(rules, child.second) }.sum()

fun main() {
    val targetColor = "shiny gold"

    println("Part1: ${(uniqueParents(bagRules, targetColor) - targetColor).size}") // 139
    println("Part2: ${childrenInstances(bagRules, targetColor) - 1}") // 58175
}