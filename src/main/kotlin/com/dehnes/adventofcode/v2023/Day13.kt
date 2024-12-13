package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Maps.mirrorHorizontal
import com.dehnes.adventofcode.utils.Maps.rotateMap90Right
import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day13 {
    val patterns = getText().split("\n\n").map { it.lines().map { it.toList().toTypedArray() }.toTypedArray() }

    @Test
    fun part1() {
        check(solve(0) == 33047L)
        check(solve(1) == 28806L)
    }

    private fun solve(errors: Int) = patterns.fold(0L) { acc, map ->
        val transformed = map.rotateMap90Right().mirrorHorizontal()

        acc +
            (1..<map[0].size).sumOf { if (mirrorsAt(map, it, errors) == errors) it else 0 } +
            (1..<transformed[0].size).sumOf { 100 * (if (mirrorsAt(transformed, it, errors) == errors) it else 0) }
    }

    private fun mirrorsAt(map: Array<Array<Char>>, at: Int, maxErrors: Int): Int {
        var errors = 0
        var left = at - 1
        var right = at
        while (left >= 0 && right < map[0].size) {
            map.indices.forEach {
                if (map[it][left] != map[it][right] && ++errors > maxErrors) return errors
            }
            left--
            right++
        }
        return errors
    }

}