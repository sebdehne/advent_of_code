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

    fun Array<Array<Char>>.mirrorHorizontal(): Array<Array<Char>> =
        this.map { line -> line.reversedArray() }.toTypedArray()

    fun Array<Array<Char>>.rotateMap90Right(): Array<Array<Char>> {
        val originalWidth = this[0].size
        val originalHeight = this.size

        val result = Array(originalWidth) { Array(originalHeight) { ' ' } }

        this.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                result[x][originalHeight - y - 1] = c
            }
        }

        return result
    }


    fun Array<Array<Char>>.sha1() = Hash.SHA1(this.flatMap { it.map { it.code.toByte() } }.toByteArray())
}