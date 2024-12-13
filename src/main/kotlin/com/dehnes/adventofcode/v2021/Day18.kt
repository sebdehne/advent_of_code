package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import kotlin.math.ceil

class Day18 {
    val input = getLines()

    @Test
    fun run() {

        val result = input.reduce { acc, s ->
            reduce(addition(acc, s))
        }
        val snailfishNumber = SnailfishNumber.parse(result) as SnailfishNumberPair

        // part 1
        check(snailfishNumber.magnitude() == 3216L)

        var highest: Long = 0
        input.forEach { numberA ->
            input.forEach { numberB ->
                if (numberA != numberB) {
                    val magnitude =
                        (SnailfishNumber.parse(reduce(addition(numberA, numberB))) as SnailfishNumberPair).magnitude()
                    if (magnitude > highest) {
                        highest = magnitude
                    }

                    val magnitude2 =
                        (SnailfishNumber.parse(reduce(addition(numberB, numberA))) as SnailfishNumberPair).magnitude()
                    if (magnitude2 > highest) {
                        highest = magnitude2
                    }

                }
            }
        }

        // part 2
        check(highest == 4643L)

    }

    fun String.replace(replacements: List<Pair<Pair<Int, Int>, String>>): String {
        var newStr = this
        replacements.sortedByDescending { it.first.first }.forEach { replacement ->
            newStr = newStr.substring(
                0,
                replacement.first.first
            ) + replacement.second + newStr.substring(replacement.first.second)
        }
        return newStr
    }

    fun reduce(str: String): String {
        var reduced = str
        while (true) {

            val firstFourPair = findFirstPairInFour(reduced)
            if (firstFourPair != null) {
                val replacements = mutableListOf<Pair<Pair<Int, Int>, String>>()
                val pair = getRegularPair(reduced, firstFourPair)
                val findRegularRight = findRegularRight(reduced, firstFourPair + pair.toLength())
                if (findRegularRight != null) {
                    val v = reduced.substring(findRegularRight.first, findRegularRight.second).toInt()
                    replacements.add(Pair(findRegularRight, (v + pair.second).toString()))
                }
                val findRegularLeft = findRegularLeft(reduced, firstFourPair)
                if (findRegularLeft != null) {
                    val v = reduced.substring(findRegularLeft.first, findRegularLeft.second).toInt()
                    replacements.add(Pair(findRegularLeft, (v + pair.first).toString()))
                }
                replacements.add(Pair(Pair(firstFourPair - 1, (firstFourPair - 1) + pair.toLength()), "0"))
                reduced = reduced.replace(replacements)
                continue
            }

            val firstAbove9 = findFirstAbove9(reduced)
            if (firstAbove9 != null) {
                val v = reduced.substring(firstAbove9.first, firstAbove9.second).toInt()
                val x = v / 2
                val y = ceil(v.toDouble() / 2).toInt()
                reduced = reduced.replace(listOf(Pair(firstAbove9, "[$x,$y]")))
                continue
            }

            break
        }

        return reduced
    }

    fun addition(a: String, b: String) = "[$a,$b]"

    private fun findFirstAbove9(str: String): Pair<Int, Int>? {
        var startNumber = -1

        str.forEachIndexed { index, c ->
            if (startNumber >= 0) {
                if (!c.isDigit()) {
                    if (index - startNumber > 1) {
                        return startNumber to index
                    }
                    startNumber = -1
                }
            } else if (c.isDigit() && startNumber == -1) {
                startNumber = index
            }
        }
        return null
    }

    private fun Pair<Int, Int>.toLength() = "[${this.first},${this.second}]".length

    private fun findRegularLeft(str: String, startIndex: Int): Pair<Int, Int>? {
        var end = -1
        ((startIndex - 1) downTo 0).forEach { i ->
            val c = str[i]
            if (c.isDigit()) {
                if (end == -1) {
                    end = i
                }
            } else {
                if (end >= 0) {
                    return i + 1 to end + 1
                }
            }
        }
        return null
    }

    private fun findRegularRight(str: String, startIndex: Int): Pair<Int, Int>? {
        var start = -1
        (startIndex until str.length).forEach { i ->
            val c = str[i]
            if (c.isDigit()) {
                if (start == -1) {
                    start = i
                }
            } else if (start >= 0) {
                return start to i
            }
        }
        return null
    }

    private fun getRegularPair(str: String, index: Int): Pair<Int, Int> {
        var first = ""
        var firstNumber: Int? = null
        var second = ""
        var secondNumber: Int? = null
        for (i in index..str.length) {
            if (firstNumber != null && secondNumber != null) {
                break
            }
            val c = str[i]
            if (c == ',') {
                firstNumber = first.toInt()
            } else if (c == ']') {
                secondNumber = second.toInt()
            } else {
                if (firstNumber == null) {
                    first += c
                } else {
                    second += c
                }
            }
        }
        return firstNumber!! to secondNumber!!
    }

    private fun findFirstPairInFour(str: String): Int? {
        var depth = 0
        str.forEachIndexed { index, c ->
            if (c.isDigit() && depth > 4) {
                return index
            }
            if (c == '[') {
                depth++
            } else if (c == ']') {
                depth--
            }
        }
        return null
    }


    sealed class SnailfishNumber {
        companion object {
            fun parse(str: String): SnailfishNumber {
                try {
                    return RegularNumber(str.toInt())
                } catch (e: NumberFormatException) {
                }
                val commaPos = findCommaPos(str)
                val pair = SnailfishNumberPair(
                    parse(str.substring(1, commaPos)),
                    parse(str.substring(commaPos + 1, str.length - 1)),
                )
                return pair
            }

            private fun findCommaPos(str: String): Int {
                var depth = 0
                str.substring(1, str.length - 1).forEachIndexed { index, c ->
                    if (depth == 0 && c == ',') {
                        return index + 1
                    } else if (c == '[') {
                        depth++
                    } else if (c == ']') {
                        depth--
                    }
                }
                error("")
            }
        }
    }

    data class RegularNumber(
        var value: Int
    ) : SnailfishNumber() {
        override fun toString(): String {
            return "$value"
        }
    }

    data class SnailfishNumberPair(
        var x: SnailfishNumber,
        var y: SnailfishNumber
    ) : SnailfishNumber() {
        fun magnitude(): Long {
            val xm = if (x is RegularNumber) {
                (x as RegularNumber).value.toLong()
            } else {
                (x as SnailfishNumberPair).magnitude()
            } * 3L
            val ym = if (y is RegularNumber) {
                (y as RegularNumber).value.toLong()
            } else {
                (y as SnailfishNumberPair).magnitude()
            } * 2L
            return xm + ym
        }

        override fun toString(): String {
            return "[$x,$y]"
        }
    }


}