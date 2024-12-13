package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day01 {

    @Test
    fun test() {
        val (l, r) = getLines().map {
            val (l, r) = it.split("   ")
            l.toInt() to r.toInt()
        }.let { it.map { it.first } to it.map { it.second } }

        check(l.sorted().zip(r.sorted()).sumOf { (l1, r1) -> (l1 - r1).absoluteValue } == 1151792)
        check(l.sumOf { ln -> ln * (r.count { it == ln }) } == 21790168)
    }

}
