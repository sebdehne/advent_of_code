package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.pow

class Day25 {

    @Test
    fun run() {
        val sum = getLines().sumOf { line ->

            val digits = line.toList().map { c ->
                when (c) {
                    '2' -> 2
                    '1' -> 1
                    '0' -> 0
                    '-' -> -1
                    '=' -> -2
                    else -> error("")
                }
            }

            val results = digits.mapIndexed { index: Int, value: Int ->
                val place = digits.size - index
                val m = 5.toDouble().pow(place - 1).toLong()
                m * value
            }
            results.sum()
        }

        val answer = sum.toSNAFU()
        check(answer == "2==0=0===02--210---1")
    }

    fun Long.toSNAFU(): String {
        val result = mutableListOf<Int>()
        var rest = this
        while (rest > 5) {
            val a = rest % 5
            result.add(a.toInt())
            rest /= 5
        }
        result.add(rest.toInt())

        return result.reversed().postprosess()
    }

    fun List<Int>.postprosess(): String {
        val result = LinkedList<Char>()
        var extra = 0
        for (i in (this.size - 1) downTo 0) {
            var v = this[i] + extra
            extra = 0
            if (v > 2) {
                extra++
                v += -5
            }
            result.add(
                when (v) {
                    -2 -> '='
                    -1 -> '-'
                    0 -> '0'
                    1 -> '1'
                    2 -> '2'
                    else -> error("")
                }
            )
        }
        if (extra > 0) {
            result.add('1')
        }

        return result.reversed().joinToString("")
    }
}