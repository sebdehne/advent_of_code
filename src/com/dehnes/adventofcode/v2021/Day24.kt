package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals


class Day24 {

    val variables = File("resources/2021/day24.txt").readLines().let { lines ->
        val v = Array(14) { IntArray(3) }
        (0..13).forEach { group ->
            v[group][0] = lines[(18 * group) + 4].split("z")[1].trim().toInt()
            v[group][1] = lines[(18 * group) + 5].split("x")[1].trim().toInt()
            v[group][2] = lines[(18 * group) + 15].split("y")[1].trim().toInt()
        }
        v
    }

    @Test
    fun run() {
        assertEquals(94992992796199, calc(true))
        assertEquals(11931881141161, calc(false))
    }

    private fun calc(highest: Boolean): Long {
        val result = IntArray(14)

        val stack = LinkedList<Int>()
        for (i in 0..13) {
            if (variables[i][0] == 1) stack.push(i) else pair(stack.pop(), i, highest, result)
        }

        return result.joinToString(separator = "") { it.toString() }.toLong()
    }

    private fun pair(fromIndex: Int, toIndex: Int, highest: Boolean, result: IntArray) {
        (1..9).forEach { input ->
            val i = if (highest) 10 - input else input
            val j = i + variables[fromIndex][2] + variables[toIndex][1]
            if (j in 1..9) {
                result[fromIndex] = i
                result[toIndex] = j
                return
            }
        }
    }

}

