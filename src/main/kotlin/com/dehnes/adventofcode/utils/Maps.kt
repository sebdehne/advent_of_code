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

    fun Array<Array<Char>>.print() {
        println(
            this.joinToString("\n") {
                it.joinToString("") { it.toString() }
            }
        )
        println()
    }

    operator fun <T> Array<Array<T>>.set(pos: PointInt, t: T) {
        this[pos.y][pos.x] = t
    }

    fun Array<Array<Char>>.copy() = Array(size) { get(it).clone() }

    fun <T> Array<Array<T>>.forEachPos(fn: (PointInt, T) -> Any) {
        this.indices.forEach { y ->
            this[0].indices.forEach { x ->
                fn(PointInt(x, y), this[y][x])
            }
        }
    }

    fun <T> Array<Array<T>>.findPos(t: T): PointInt? {
        this.indices.forEach { y ->
            this[0].indices.forEach { x ->
                if (this[y][x] == t) {
                    return PointInt(x, y)
                }
            }
        }
        return null
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

    fun Array<Array<Char>>.sha1() = Hash.sha1(this.flatMap { it.map { it.code.toByte() } }.toByteArray())
    fun Array<Array<Char>>.get(pos: PointInt) = this[pos.y][pos.x]
}