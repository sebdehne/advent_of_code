package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day06 {
    val map = getLines().fold(emptyMap<String, String>()) { acc, line ->
        val (parent, child) = line.split(")")
        acc + (child to parent)
    }

    @Test
    fun main() {
        check(map.keys.fold(0L) { acc, key -> acc + parents(map, key).size } == 308790L)

        val closestCommonParent =
            parents(map, "YOU").intersect(parents(map, "SAN")).maxByOrNull { parents(map, it).size }!!
        check(parents(map, "YOU", closestCommonParent).size + parents(map, "SAN", closestCommonParent).size - 2 == 472)
    }

    fun parents(map: Map<String, String>, from: String, stopAt: String? = null): List<String> {
        val parents = mutableListOf<String>()
        var next = from
        while (true) {
            if (map[next] != null) {
                parents.add(map[next]!!)
            }
            if (map[next] == null || map[next] == stopAt)
                break
            next = parents.last()
        }
        return parents
    }

}