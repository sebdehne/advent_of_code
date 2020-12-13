package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day11 {
    val originalMap = File("resources/day11.txt").readLines().map { it.toList() }
    val length = originalMap.first().size

    fun Pair<Int, Int>.next() = this.first + (this.second + 1) / length to (this.second + 1) % length

    fun List<List<Char>>.deepClone() = this.map { it.toMutableList() }.toMutableList()

    fun Pair<Int, Int>.countAdjacentOccupied(map: List<List<Char>>, limitedDepth: Boolean): Int {
        var occupied = 0
        for (dir1 in -1..1) {
            for (dir2 in -1..1) {
                var depth = 1
                while (dir1 != 0 || dir2 != 0) {
                    val c = map.getOrNull((this.first + dir1 * depth))?.getOrNull((this.second + dir2 * depth))
                    if (c != '.') {
                        if (c == '#') {
                            occupied++
                        }
                        break
                    }
                    if (limitedDepth) {
                        break
                    }
                    depth++
                }
            }
        }

        return occupied
    }

    fun test(minAdjacentOccupied: Int, limitedDepth: Boolean): List<List<Char>> {
        var currentMap = originalMap
        val updatedMap = currentMap.deepClone()
        while (true) {
            var pos = 0 to 0
            var changed = false
            while (true) {
                val c = currentMap.getOrNull(pos.first)?.getOrNull(pos.second) ?: break
                if (c != '.') {
                    val adjacentOccupied = pos.countAdjacentOccupied(currentMap, limitedDepth)
                    if (c == 'L' && adjacentOccupied == 0) {
                        updatedMap[pos.first][pos.second] = '#'
                        changed = true
                    } else if (c == '#' && adjacentOccupied > minAdjacentOccupied) {
                        updatedMap[pos.first][pos.second] = 'L'
                        changed = true
                    }
                }
                pos = pos.next()
            }
            if (!changed) {
                break
            }
            currentMap = updatedMap.deepClone()
        }
        return updatedMap
    }

    @Test
    fun main() {
        val part1Map = test(3, true)
        println(part1Map.flatten().count { it == '#' }) // 2418
        val part2Map = test(4, false)
        println(part2Map.flatten().count { it == '#' }) // 2144
    }
}