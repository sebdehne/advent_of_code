package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.absoluteValue

class Day12 {
    val instructions = File("resources/2020/day12.txt").readLines().map { dirAndLength ->
        dirAndLength[0] to dirAndLength.substring(1).toInt()
    }

    fun multiplier(dir: Char) = when (dir) {
        'N' -> 1 to 0
        'S' -> -1 to 0
        'E' -> 0 to 1
        'W' -> 0 to -1
        else -> error("")
    }

    val rotateFOrderRight = listOf('E', 'S', 'W', 'N')
    fun rotateF(f: Char, degrees: Int, left: Boolean) =
            rotateFOrderRight[(rotateFOrderRight.indexOf(f) + (if (left) 360 - degrees else degrees) / 90) % 4]

    @Test
    fun main() {
        var forwardsMeans = 'E'
        val part1 = instructions.fold(0 to 0) { pos, (direction, length) ->
            when (direction) {
                'L' -> {
                    forwardsMeans = rotateF(forwardsMeans, length, true)
                    pos
                }
                'R' -> {
                    forwardsMeans = rotateF(forwardsMeans, length, false)
                    pos
                }
                else -> {
                    pos + (multiplier(if (direction == 'F') forwardsMeans else direction) * length)
                }
            }
        }

        println(part1.first.absoluteValue + part1.second.absoluteValue) // 1010

        var wp = 1 to 10
        val part2 = instructions.fold(0 to 0) { pos, (direction, length) ->
            when (direction) {
                'F' -> {
                    pos + wp * length
                }
                'R' -> {
                    wp = wp.rotate(length, false)
                    pos
                }
                'L' -> {
                    wp = wp.rotate(length, true)
                    pos
                }
                else -> {
                    wp += multiplier(direction) * length
                    pos
                }
            }
        }

        println(part2.first.absoluteValue + part2.second.absoluteValue) // 52742
    }

    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = this.first + other.first to this.second + other.second
    operator fun Pair<Int, Int>.times(other: Int) = this.first * other to this.second * other

    fun Pair<Int, Int>.rotate(degrees: Int, left: Boolean): Pair<Int, Int> {
        val t = (if (left) 360 - degrees else degrees) / 90 % 4

        var newPos = this
        for (i in t downTo 1) {
            newPos = newPos.second * -1 to newPos.first
        }

        return newPos
    }
}