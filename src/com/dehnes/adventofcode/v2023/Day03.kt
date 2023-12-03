package com.dehnes.adventofcode.v2023

import org.junit.jupiter.api.Test
import java.io.File

class Day03 {

    val map = File("resources/2023/day03.txt").readLines().map {
        it.toCharArray()
    }.toTypedArray()

    fun surroundingSymbols(x: Int, y: Int) = listOf(
        y - 1 to x - 1,
        y to x - 1,
        y + 1 to x - 1,
        y - 1 to x,
        y + 1 to x,
        y - 1 to x + 1,
        y to x + 1,
        y + 1 to x + 1,
    ).mapNotNull { pos ->
        map.getOrNull(pos.first)?.getOrNull(pos.second)?.let {
            pos to it
        }
    }
        .filterNot { it.second.isDigit() }
        .filterNot { it.second == '.' }

    @Test
    fun part1() {

        var currentNumber = ""
        val partNumbers = mutableListOf<Int>()
        var symbolFound = false

        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->


                if (currentNumber.length > 0 && !c.isDigit()) {
                    // ended
                    if (symbolFound) {
                        partNumbers.add(currentNumber.toInt())
                    }
                    symbolFound = false
                    currentNumber = ""
                } else if (currentNumber.length > 0 && c.isDigit()) {
                    // continuing

                    currentNumber += c
                    if (!symbolFound && surroundingSymbols(x, y).any()) {
                        symbolFound = true
                    }
                } else if (currentNumber.length == 0 && c.isDigit()) {
                    // starting

                    currentNumber += c
                    if (!symbolFound && surroundingSymbols(x, y).any()) {
                        symbolFound = true
                    }
                }
            }

            if (symbolFound) {
                partNumbers.add(currentNumber.toInt())
            }
            symbolFound = false
            currentNumber = ""
        }

        check(partNumbers.sum() == 540131)
    }


    @Test
    fun part2() {
        var currentNumber = ""
        val gears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        var symbolFound = false
        val currentStars = mutableSetOf<Pair<Int, Int>>()

        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->

                if (currentNumber.length > 0 && !c.isDigit()) {
                    // ended
                    if (symbolFound) {
                        currentStars.forEach { starPos ->
                            gears.getOrPut(starPos) { mutableListOf() }
                            gears[starPos]!!.add(currentNumber.toInt())
                        }
                    }
                    symbolFound = false
                    currentNumber = ""
                    currentStars.clear()
                } else if (currentNumber.length > 0 && c.isDigit()) {
                    // continuing

                    currentNumber += c
                    val surroundingSymbols = surroundingSymbols(x, y)
                    if (surroundingSymbols.any()) {
                        symbolFound = true
                        currentStars.addAll(surroundingSymbols.filter { it.second == '*' }.map { it.first })
                    }
                } else if (currentNumber.length == 0 && c.isDigit()) {
                    // starting

                    currentNumber += c
                    val surroundingSymbols = surroundingSymbols(x, y)
                    if (surroundingSymbols.any()) {
                        symbolFound = true
                        currentStars.addAll(surroundingSymbols.filter { it.second == '*' }.map { it.first })
                    }
                }
            }

            if (symbolFound) {
                currentStars.forEach { starPos ->
                    gears.getOrPut(starPos) { mutableListOf() }
                    gears[starPos]!!.add(currentNumber.toInt())
                }
            }
            symbolFound = false
            currentNumber = ""
            currentStars.clear()
        }

        val result = gears
            .filter { it.value.size > 1 }
            .map { it.value.reduce { acc, i -> acc * i } }
            .sum()
        check(result == 86879020)
    }
}