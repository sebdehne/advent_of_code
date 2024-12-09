package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day09 {
    val map = mutableListOf<Int>()

    init {
        getText().trim().forEachIndexed { index, c ->
            repeat(c.toString().toInt()) { map.add(if (index % 2 == 0) index / 2 else -1) }
        }
    }

    @Test
    fun part1() {
        var freeSpacePos = 0
        var i = map.size - 1
        var phase1 = true
        while (true) {
            if (phase1) {
                while (++freeSpacePos < map.size && map[freeSpacePos] != -1) {}
                phase1 = false
            } else {
                val c = map[i]
                if (c != -1) {
                    map[freeSpacePos] = c
                    map[i] = -1
                }
                while (map[--i] == -1) {}
                phase1 = true
            }
            if (i <= freeSpacePos) break
        }

        val part1 = map.indices.mapNotNull { i -> if (map[i] == -1) null else map[i].toLong() * i }.sum()
        check(6390180901651 == part1)
    }

    @Test
    fun part2() {
        val files = mutableListOf<File>()
        val spaces = mutableListOf<Pair<Int, Int>>()
        var start = 0
        var currentId = map[0]
        map.forEachIndexed { index, i ->
            when {
                i == currentId -> {
                    // continue space or file
                }

                currentId == -1 -> {
                    // space -> file
                    val len = index - start
                    spaces.add(start to len)
                    start = index
                    currentId = i
                }

                i == -1 -> {
                    // file -> space
                    val len = index - start
                    files.add(File(currentId, start, len))
                    start = index
                    currentId = i
                }

                else -> {
                    // file -> file
                    val len = index - start
                    files.add(File(currentId, start, len))
                    start = index
                    currentId = i
                }
            }
        }
        val len = map.size - start
        if (len > 0) {
            if (currentId == -1) {
                spaces.add(start to len)
            } else {
                files.add(File(currentId, start, len))
            }
        }

        // move files into spaces and correct/merge remaining spaces
        files.reversed().forEach { f ->
            val spaceI = spaces.indexOfFirst { s -> s.second >= f.blocks }
            if (spaceI >= 0) {
                val space = spaces[spaceI]
                if (space.first > f.pos) return@forEach
                val remaining = space.second - f.blocks
                if (remaining > 0) {
                    spaces[spaceI] = (space.first + f.blocks) to remaining
                } else {
                    spaces.removeAt(spaceI)
                }
                val p = f.pos
                f.pos = space.first

                // add space again
                val before = spaces.indexOfFirst { s -> (s.first + s.second) == p }
                val after = spaces.indexOfFirst { s -> p + f.blocks == s.first }
                if (before != -1 && after != -1) {
                    val b = spaces[before]
                    val a = spaces[after]
                    spaces[before] = b.first to (a.second + f.blocks + b.second)
                    spaces.removeAt(after)
                } else if (before != -1) {
                    val b = spaces[before]
                    spaces[before] = b.first to (f.blocks + b.second)
                } else if (after != -1) {
                    val a = spaces[after]
                    spaces[after] = p to (f.blocks + a.second)
                } else {
                    spaces.add(p to f.blocks)
                }
            }
        }

        val part2 = files.flatMap { f -> (0..<f.blocks).map { b -> ((f.pos + b).toLong()) * f.id } }.sum()
        check(6412390114238 == part2)
    }

    data class File(val id: Int, var pos: Int, val blocks: Int)
}