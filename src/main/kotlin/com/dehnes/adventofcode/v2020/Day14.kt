package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day14 {

    val lines = File("resources/2020/day14.txt").readLines()

    @Test
    fun part1() {
        val memory = mutableMapOf<String, Long>()
        var mask = 0L to 0L

        lines.forEach { line ->
            if (line.startsWith("mask")) {
                mask = 0L to 0L
                line.toList().reversed().forEachIndexed { index, c ->
                    if (c == '0') {
                        mask = mask.first to (mask.second or 1L.shl(index))
                    } else if (c == '1') {
                        mask = (mask.first or 1L.shl(index)) to mask.second
                    }
                }
            } else {
                val mem = "mem\\[(\\d+)] = (\\d+)".toRegex().find(line)
                var value = mem!!.groupValues[2].toLong()
                value = value or mask.first
                value = value and mask.second.inv()
                memory[mem.groupValues[1]] = value
            }
        }

        println(memory.values.sum()) // 4297467072083
    }

    @Test
    fun part2() {

        var mask = 0L to emptyList<Int>()
        val memory = mutableMapOf<Long, Long>()

        lines.forEach { line ->
            val mem = "mem\\[(\\d+)] = (\\d+)".toRegex().find(line)
            if (line.startsWith("mask")) {
                mask = 0L to emptyList()
                line.toList().reversed().forEachIndexed { index, c ->
                    if (c == '1') {
                        mask = (mask.first or 1L.shl(index)) to mask.second
                    } else if (c == 'X') {
                        mask = mask.first to (mask.second + index)
                    }
                }
            } else {
                var addr = mem!!.groupValues[1].toLong()
                addr = addr or mask.first

                mask.second.map { i -> listOf(i to 0, i to 1) }.combinations { mask ->
                    var addr2 = addr

                    addr2 = addr2 or mask.filter { it.second == 1 }.fold(0L) { acc, (index, _) ->
                        acc or 1L.shl(index)
                    }

                    addr2 = addr2 and mask.filter { it.second == 0 }.fold(0L) { acc, (index, _) ->
                        acc or 1L.shl(index)
                    }.inv()

                    memory[addr2] = mem.groupValues[2].toLong()
                }

            }
        }

        println(memory.values.sum()) // 5030603328768
    }

    fun <T> List<List<T>>.combinations(current: List<T> = emptyList(), fn: (List<T>) -> Unit) {
        if (current.size < this.size) {
            this[current.size].forEach { item -> this.combinations(current + item, fn) }
        } else {
            fn(current)
        }
    }

}