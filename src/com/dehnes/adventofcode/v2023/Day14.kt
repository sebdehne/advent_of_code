package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Maps.rotateMap90Right
import com.dehnes.adventofcode.utils.Maps.sha1
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test


class Day14 {

    @Test
    fun part1() {
        val map = getLines().map { it.toCharArray() }.toTypedArray()
        map.tiltNorth()

        check(map.countResult() == 107142)
    }

    @Test
    fun part2() {
        var map = getLines().map { it.toCharArray() }.toTypedArray()

        val hashToOffset = mutableMapOf<String, Int>()
        val result: Int
        var count = 1
        while (true) {
            repeat(4) {
                map.tiltNorth()
                map = map.rotateMap90Right()
            }

            val key = map.sha1()
            if (key in hashToOffset) {
                val offset = hashToOffset[key]!!
                if ((1000000000 - offset).mod(count - offset) == 0) {
                    result = map.countResult()
                    break
                }
            } else {
                hashToOffset[key] = count
            }

            count++
        }

        check(result == 104815)
    }

    private fun Array<CharArray>.tiltNorth() {
        this[0].indices.forEach { x ->

            var pos = 0
            var y = 0
            var searching = false
            while (y < this.size) {
                val c = this[y][x]

                when {
                    c == '.' -> {
                        searching = true
                        y++
                    }
                    searching && c == 'O' -> {
                        searching = false
                        this[y][x] = '.'
                        this[pos][x] = 'O'
                        pos++
                        y = pos
                    }
                    searching && c == '#' -> {
                        searching = false
                        y++
                        pos = y
                    }
                    c != '.' -> {
                        pos++
                        y++
                    }
                }
            }
        }
    }


    private fun Array<CharArray>.countResult() = this[0].indices.sumOf { x ->
        this.indices.sumOf { y -> if (this[y][x] == 'O') (this.size - y) else 0 }
    }

}