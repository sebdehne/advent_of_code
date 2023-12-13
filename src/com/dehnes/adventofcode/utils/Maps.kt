package com.dehnes.adventofcode.utils

object Maps {

    fun Array<CharArray>.mirrorHorizontal(): Array<CharArray> = this.map { line -> line.reversedArray() }.toTypedArray()

    fun Array<CharArray>.rotateMap90Right(): Array<CharArray> {
        val originalWidth = this[0].size
        val originalHeight = this.size

        val result = Array(originalWidth) { CharArray(originalHeight) }

        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                result[x][originalHeight - y - 1] = c
            }
        }

        return result
    }


}