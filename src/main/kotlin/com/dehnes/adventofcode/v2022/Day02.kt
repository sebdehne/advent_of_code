package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day02 {

    @Test
    fun run() {
        var part1 = 0L
        var part2 = 0L
        getLines().forEach { line ->
            val hs1 = line[0] - 'A'

            val play = { hs2: Int ->
                val win = (hs1 + 1).mod(3) == hs2
                when {
                    hs1 == hs2 -> 3
                    win -> 6
                    else -> 0
                } + (hs2 + 1)
            }

            part1 += play(line[2] - 'X')
            part2 += play(((hs1 + 3) + (line[2] - 'Y')).mod(3))
        }
        //check(part1== 15L)
        //check(part2== 12L)

        check(9759L == part1)
        check(12429L == part2)
    }

}