package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*

class Day20 {

    @Test
    fun run() {
        check(solve(1, 1) == 16533L)
        check(solve(10, 811589153) == 4789999181006L)
    }

    fun solve(rounds: Int, key: Long): Long {
        val originalNumbers = getLines().map { it.toLong() * key }
        val ring = LinkedList<Pair<Int, Long>>()
        originalNumbers.forEachIndexed { index, i ->
            ring.addLast(index to i)
        }

        repeat(rounds) {
            originalNumbers.forEachIndexed { index, i ->
                val pos = ring.indexOfFirst { it.first == index }
                var newPos = ((pos + i) % (originalNumbers.size - 1)).toInt()
                if (newPos < 0) {
                    newPos += (originalNumbers.size - 1)
                }

                ring.removeAt(pos)
                ring.add(newPos, index to i)
            }
        }


        return listOf(1000, 2000, 3000).sumOf {
            ring[(ring.indexOfFirst { it.second == 0L } + it) % originalNumbers.size].second
        }
    }
}

