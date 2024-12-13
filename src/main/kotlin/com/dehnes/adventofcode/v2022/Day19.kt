package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day19 {

    @Test
    fun runPart1() {
        val blueprints = parse()

        var qualityLevel = 0

        blueprints.forEachIndexed { index, blueprint ->
            val result = solveLagrestGeodes(blueprint, 5000)
            qualityLevel += (result * (index + 1))
        }

        check(qualityLevel == 1681)
    }

    @Test
    fun runPart2() {
        val blueprints = parse().subList(0, 3)

        val keepInMemSize = 70
        val minutes = 32
        val result = solveLagrestGeodes(blueprints[0], keepInMemSize, minutes)
        val result2 = solveLagrestGeodes(blueprints[1], keepInMemSize, minutes)
        val result3 = solveLagrestGeodes(blueprints[2], keepInMemSize, minutes)

        check(result * result2 * result3 == 5394)
    }

    fun solveLagrestGeodes(b: Blueprint, keepInMemSize: Int = 10000, minutes: Int = 24): Int {
        var p = listOf(
            Progress(mapOf(), mapOf(Resource.ore to 1))
        )

        (1..minutes).forEach { minute ->
            p = p.map { runNextMinute(b, it) }.flatten()

            if (p.size > keepInMemSize) {
                val newP = p.sortedByDescending { it.calcValue(b, minutes - minute) }
                p = newP.dropLast(p.size - keepInMemSize)
            }
        }

        return p.maxOf { it.resourceInventory[Resource.geode] ?: 0 }
    }

    fun runNextMinute(b: Blueprint, p: Progress): List<Progress> {

        val canBuild = Resource.values().filter { resource ->
            val robotInQuestion = b.first { it.produces == resource }

            robotInQuestion.costs.all { c ->
                (p.resourceInventory[c.first] ?: 0) >= c.second
            }
        }

        val result = mutableListOf<Progress>()
        result.add(p.cycle())

        canBuild.forEach { robotToBuild ->
            val robotInQuestion = b.first { it.produces == robotToBuild }
            result.add(p.cycleAndBuild(robotToBuild, robotInQuestion.costs))
        }

        return result
    }

    fun parse() = getLines().map { line ->
        val parts = line.split(".", ":")
        (1..4).map { i ->
            val rParts = parts[i].split(" ")

            val costs = mutableListOf<Cost>()
            costs.add(Resource.valueOf(rParts[6]) to rParts[5].toInt())
            if (rParts.size > 7) {
                costs.add(Resource.valueOf(rParts[9]) to rParts[8].toInt())
            }

            Robot(Resource.valueOf(rParts[2]), costs)
        }
    }

    fun Resource.getValue(b: Blueprint): Int {
        return if (this == Resource.ore) 1 else {
            b.first { it.produces == this }.costs.fold(0) { acc, c ->
                acc + (c.first.getValue(b) * c.second)
            }
        }
    }

    fun Progress.calcValue(b: Blueprint, minutesRemaining: Int): Int {

        val robotValues = robotInventory.entries.fold(0) { acc, entry ->
            acc + ((entry.key.getValue(b) * entry.value) * minutesRemaining)
        }
        val inventoryValues = resourceInventory.entries.fold(0) { acc, entry ->
            acc + (entry.key.getValue(b) * entry.value)
        }

        return robotValues + inventoryValues
    }
}

data class Progress(
    val resourceInventory: Map<Resource, Int>,
    val robotInventory: Map<Resource, Int>,
) {
    fun cycle(): Progress {

        val updatedResourceInventory = resourceInventory.toMutableMap()

        Resource.values().forEach { r ->
            updatedResourceInventory[r] = (updatedResourceInventory[r] ?: 0) + (robotInventory[r] ?: 0)
        }

        return Progress(
            updatedResourceInventory,
            robotInventory,
        )
    }

    fun cycleAndBuild(robotType: Resource, costs: List<Cost>): Progress {
        val updatedResourceInventory = resourceInventory.toMutableMap()
        costs.forEach { c ->
            updatedResourceInventory[c.first] = updatedResourceInventory[c.first]!! - c.second
        }

        Resource.values().forEach { r ->
            updatedResourceInventory[r] = (updatedResourceInventory[r] ?: 0) + (robotInventory[r] ?: 0)
        }

        return Progress(
            updatedResourceInventory,
            robotInventory + (robotType to ((robotInventory[robotType] ?: 0) + 1)),
        )
    }

    override fun toString(): String {
        return "Progress(resourceInventory=$resourceInventory, robotInventory=$robotInventory)"
    }

}

typealias Cost = Pair<Resource, Int>
typealias Blueprint = List<Robot>


enum class Resource {
    ore,
    clay,
    obsidian,
    geode
}


data class Robot(
    val produces: Resource,
    val costs: List<Cost>,
)
