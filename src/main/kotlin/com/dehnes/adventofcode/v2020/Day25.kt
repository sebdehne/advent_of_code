package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test

class Day25 {

    @Test
    fun part1() {
        val input1 = 1965712L
        val input2 = 19072108L

        val cardLoopSize = findLoopSize(input1, 7)
        println((1..cardLoopSize).fold(1L) { acc, _ ->
            (acc * input2) % 20201227
        })// 16881444
    }

    fun findLoopSize(publicKey: Long, subject: Long): Int {
        var value = 1L
        return generateSequence(1) { it + 1 }.first {
            value = (value * subject) % 20201227
            value == publicKey
        }
    }

}