package com.dehnes.adventofcode_2020.day07

import java.io.File

data class BagRule(
        val color: String,
        val contains: List<Pair<Int, String>>
)

fun uniqueParents(map: List<BagRule>, rule: BagRule): Set<String> = map
        .filter { other -> other.contains.any { it.second == rule.color } }.map { parent -> uniqueParents(map, parent) }
        .flatten()
        .toSet() + rule.color

fun childrenInstances(map: List<BagRule>, rule: BagRule): Long =
        1 + rule.contains.map { child -> child.first * childrenInstances(map, map.first { r -> r.color == child.second }) }.sum()

fun main() {
    val bagRules = File("resources/day07.txt").readLines().map { ruleStr ->
        ruleStr.split(" ", ",", ".").filter(String::isNotBlank).let { split ->
            BagRule(
                    "${split[0]} ${split[1]}",
                    (4 until split.size step 4).map {
                        split[it].replace("no", "0").toInt() to "${split[it + 1]} ${split[it + 2]}"
                    }.filter { p -> p.first > 0 }
            )
        }
    }

    val targetColor = "shiny gold"
    val targetRule = bagRules.first { r -> r.color == targetColor }

    println("Part1: ${(uniqueParents(bagRules, targetRule) - targetColor).size}") // 139
    println("Part2: ${childrenInstances(bagRules, targetRule) - 1}") // 58175
}