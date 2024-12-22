package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ListUtils.generate
import com.dehnes.adventofcode.utils.ListUtils.iterate
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day22 {

    @Test
    fun run() {
        val buyers = getLines().map { it.toLong() }

        check(16894083306L == buyers.sumOf { iterate(2000, it, this::calc) })

        val common = mutableMapOf<Sequence, Int>()
        buyers.forEach { sequencesAndBananas(it).forEach { (s, bananas) -> common[s] = (common[s] ?: 0) + bananas } }

        check(1925 == common.entries.maxByOrNull { it.value }!!.value)
    }

    private fun sequencesAndBananas(input: Long): Map<Sequence, Int> {
        val result = mutableMapOf<Sequence, Int>()
        var tmp = input
        generate(2000) {
            val previousPrice = tmp.price()
            val newPrice = calc(tmp).apply { tmp = this }.price()

            newPrice to newPrice - previousPrice
        }.windowed(4, 1).forEach { (a, b, c, d) ->
            result.putIfAbsent(Sequence(a.second, b.second, c.second, d.second), d.first)
        }

        return result
    }

    data class Sequence(val a: Int, val b: Int, val c: Int, val d: Int)

    private fun Long.price() = this.toString().last().toString().toInt()

    private fun calc(num: Long): Long = listOf<(Long) -> Long>(
        { n -> ((n shl 6) xor n) % (1 shl 24) },
        { n -> ((n shr 5) xor n) % (1 shl 24) },
        { n -> ((n shl 11) xor n) % (1 shl 24) },
    ).fold(num) { acc, n -> n(acc) }
}