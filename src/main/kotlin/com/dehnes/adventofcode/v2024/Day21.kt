package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Maps.forEachPos
import com.dehnes.adventofcode.utils.Maps.get
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day21 {
    val numPads = listOf(
        listOf(
            "789",
            "456",
            "123",
            " 0A",
        ).map { it.toList().toTypedArray() }.toTypedArray(),
        listOf(
            " ^A",
            "<v>",
        ).map { it.toList().toTypedArray() }.toTypedArray()
    )
    val codes = getLines()

    @Test
    fun run() {
        check(213536L == solve(2))
        check(258369757013802L == solve(25))
    }

    private fun solve(levels: Int): Long {
        val cache = mutableMapOf<String, Long>()
        fun len(s: String, level: Int): Long = cache.getOrPut("$s-$level") {
            if (level > levels) {
                return s.length.toLong()
            }
            val keyPadId = if (level == 0) 0 else 1
            var total = 0L
            var pos = 'A'
            s.forEach { target ->
                val paths = paths(keyPadId, pos, target)
                total += paths.map { path -> len(path + 'A', level + 1) }.minOf { it }
                pos = target
            }

            total
        }

        return codes.sumOf { len(it, 0) * it.replace("A", "").toInt() }
    }

    val pathsCache = mutableMapOf<String, List<String>>()
    private fun paths(keyPadId: Int, from: Char, to: Char): List<String> = pathsCache.getOrPut("$keyPadId-$from-$to") {
        val keyPad = numPads[keyPadId]
        var srcPos = PointInt(0, 0)
        var dstPos = PointInt(0, 0)
        keyPad.forEachPos { pos, v ->
            if (v == from) {
                srcPos = pos
            }
            if (v == to) {
                dstPos = pos
            }
        }
        var delta = dstPos - srcPos
        if (delta.y == 0) { // horizontal
            listOf((0..<(delta.x.absoluteValue)).map { if (delta.x > 0) '>' else '<' }.joinToString(""))
        } else if (delta.x == 0) {  // vertical
            listOf((0..<(delta.y.absoluteValue)).map { if (delta.y > 0) 'v' else '^' }.joinToString(""))
        } else {
            val result = mutableListOf<String>()
            if (keyPad.get(PointInt(srcPos.x, dstPos.y)) != ' ') {
                result.add(
                    (0..<(delta.y.absoluteValue)).map { if (delta.y > 0) 'v' else '^' }.joinToString("") +
                            (0..<(delta.x.absoluteValue)).map { if (delta.x > 0) '>' else '<' }.joinToString("")
                )
            }
            if (keyPad.get(PointInt(dstPos.x, srcPos.y)) != ' ') {
                result.add(
                    (0..<(delta.x.absoluteValue)).map { if (delta.x > 0) '>' else '<' }.joinToString("") +
                            (0..<(delta.y.absoluteValue)).map { if (delta.y > 0) 'v' else '^' }.joinToString("")
                )
            }
            result
        }

    }


}