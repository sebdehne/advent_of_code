package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day14 {

    val initialPairList = getLines().first().trim().toList().zipWithNext()
    val rules = Array(26) { CharArray(26) { '0' } }

    init {
        getLines().filter { it.contains(" -> ") }
            .map { it.split(" -> ").let { it[0].let { it.first() to it.last() } to it[1].first() } }
            .forEach { (pair, insertChar) -> rules[pair.first.code - 65][pair.second.code - 65] = insertChar }
    }

    @Test
    fun test() {
        check(getScoreFor(10) == 3555L)
        check(getScoreFor(40) == 4439442043739L)
    }

    private fun getScoreFor(times: Int): Long {
        var pairOccurrences = mutableMapOf<Pair<Char, Char>, Long>()
        initialPairList.forEach { pairOccurrences[it] = pairOccurrences.getOrDefault(it, 0) + 1 }
        var lastPair = initialPairList.last()

        repeat(times) {
            val newMap = mutableMapOf<Pair<Char, Char>, Long>()
            pairOccurrences.forEach { (pair, existingCount) ->
                val insert = rules[pair.first.code - 65][pair.second.code - 65]
                val newleft = pair.first to insert
                val newright = insert to pair.second
                newMap[newleft] = newMap.getOrDefault(newleft, 0) + existingCount
                newMap[newright] = newMap.getOrDefault(newright, 0) + existingCount
                if (pair == lastPair) {
                    lastPair = newright
                }
            }
            pairOccurrences = newMap
        }

        val occurences = LongArray(26)
        pairOccurrences.forEach {
            occurences[it.key.first.code - 65] += it.value
            if (it.key == lastPair) {
                occurences[it.key.second.code - 65]++
            }
        }

        val sortedBy = occurences
            .mapIndexed { index, l -> (65 + index).toChar() to l }
            .filter { it.second > 0 }.sortedBy { it.second }
        return sortedBy.last().second - sortedBy.first().second
    }
}


