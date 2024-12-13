package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day11 {

    @Test
    fun run() {
        val stones = getText().trim().split(" ").map { it.toLong() }.toList()

        check(228668L == stones.sumOf { cachedTransform(it, 25) })
        check(270673834779359L == stones.sumOf { cachedTransform(it, 75) })
    }

    data class CacheKey(val n: Long, val blinks: Int, val maxBlinks: Int)

    private val cache = mutableMapOf<CacheKey, Long>()

    private fun cachedTransform(number: Long, maxBlinks: Int, blink: Int = 1): Long =
        cache.getOrPut(CacheKey(number, blink, maxBlinks)) {
            transform(number, maxBlinks, blink)
        }

    private fun transform(number: Long, maxBlinks: Int, blink: Int): Long = single(number).let { (left, right) ->
        if (right == null) {
            if (blink == maxBlinks) 1L else cachedTransform(left, maxBlinks, blink + 1)
        } else {
            if (blink == maxBlinks) 2L else cachedTransform(left, maxBlinks, blink + 1) + cachedTransform(right, maxBlinks, blink + 1)
        }
    }

    private fun single(l: Long): Pair<Long, Long?> = when {
        l == 0L -> 1L to null
        l.toString().toList().size % 2 == 0 -> {
            val list = l.toString().toList()
            val left = list.subList(0, list.size / 2)
            val right = list.subList(list.size / 2, list.size)
            left.joinToString("").toLong() to right.joinToString("").toLong()
        }

        else -> l * 2024L to null
    }

}