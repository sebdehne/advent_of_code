package com.dehnes.adventofcode.utils

object Maps {

    fun Array<CharArray>.print() {
        println(
            this.joinToString("\n") {
                it.joinToString("") { it.toString() }
            }
        )
        println()
    }

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


    fun Array<CharArray>.sha1() = Hash.SHA1(this.flatMap { it.map { it.code.toByte() } }.toByteArray())
}