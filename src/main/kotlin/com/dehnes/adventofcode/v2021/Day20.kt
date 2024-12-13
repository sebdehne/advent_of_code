package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day20 {

    val mapping = getLines().first().mapIndexed { index, c ->
        index to (if (c == '#') 1 else 0)
    }.toMap()

    val image = getLines().drop(2).map {
        it.toList().map { if (it == '#') 1 else 0 }.toIntArray()
    }.toTypedArray()


    @Test
    fun run() {
        check(enhance(2) == 5619)
        check(enhance(50) == 20122)
    }

    private fun enhance(times: Int): Int {
        var defaultPixel = 0
        var image = image
        repeat(times) {
            image = image.grow().enhance(defaultPixel)
            defaultPixel = mapping[if (defaultPixel == 0) 0 else 511]!!
        }
        return image.sumOf { it.sum() }
    }

    fun Array<IntArray>.grow(): Array<IntArray> {
        val n = Array(this.size + 4) { IntArray(this[0].size + 4) }
        this.forEachIndexed { index, booleans ->
            System.arraycopy(this[index], 0, n[index + 2], 2, booleans.size)
        }
        return n
    }

    fun Array<IntArray>.enhance(defaultPixel: Int): Array<IntArray> {
        val copy = Array(this.size) { IntArray(this[0].size) { defaultPixel } }

        val p = { y: Int, x: Int ->
            if (y in 2 until (this.size - 2) && x in 2 until (this[0].size - 2)) {
                this[y][x]
            } else {
                defaultPixel
            }
        }

        for (y in (0..this.size - 3)) {
            for (x in (0..this[0].size - 3)) {

                val bin = "${p(y, x)}${p(y, x + 1)}${p(y, x + 2)}" +
                        "${p(y + 1, x)}${p(y + 1, x + 1)}${p(y + 1, x + 2)}" +
                        "${p(y + 2, x)}${p(y + 2, x + 1)}${p(y + 2, x + 2)}"

                val mapped = mapping[bin.toInt(2)]!!
                copy[y + 1][x + 1] = mapped
            }
        }

        val newDefaultPixel = mapping[if (defaultPixel == 0) 0 else 511]!!
        for (y in copy.indices) {
            for (x in copy[0].indices) {
                if (y !in (1 until this.size - 1) || x !in (1 until this[0].size - 1)) {
                    copy[y][x] = newDefaultPixel
                }
            }
        }

        return copy
    }
}