package com.dehnes.adventofcode.v2019

import org.junit.jupiter.api.Test
import java.io.File

class Day08 {

    @Test
    fun main() {
        val pic = File("resources/day08.txt").readText().map { it.toString().toInt() }
        val width = 25
        val height = 6

        val layers = pic.chunked(width * height)

        val layer = layers.mapIndexed { index, list ->
            index to list.count { it == 0 }
        }.minByOrNull { it.second }!!.first

        println(layers[layer].count { it == 1 } * layers[layer].count { it == 2 }) // 1950

        val resultPic = layers.reduce { acc, list ->
            acc.zip(list).map {
                if (it.first == 2) it.second else {
                    if (it.second == 2) it.first else {
                        it.first
                    }
                }
            }
        }

        resultPic.chunked(width).forEach { line ->
            println(line.joinToString("") { pixel ->
                when (pixel) {
                    0 -> " "
                    1 -> "#"
                    2 -> " "
                    else -> error("")
                }
            })
        } // FKAHL

    }
}