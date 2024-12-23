package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day23 {

    @Test
    fun run() {
        val map = mutableMapOf<String, Computer>()
        getLines().map { it.split("-") }.forEach { (cp1, cp2) ->
            map.getOrPut(cp1) { Computer(cp1) }.connections.add(cp2)
            map.getOrPut(cp2) { Computer(cp2) }.connections.add(cp1)
        }

        var visited = mutableSetOf<List<String>>()
        fun findSet(computer: String, set: List<String>, onSet: (s: List<String>) -> Unit) {
            map[computer]!!.connections.forEach { c ->
                if (c !in set) {
                    if (set.all { other -> c in map[other]!!.connections }) {
                        val newSet = (set + c).sorted()
                        if (newSet !in visited) {
                            onSet(newSet)
                            visited.add(newSet)
                            findSet(c, newSet, onSet)
                        }
                    }
                }
            }
        }

        var allSets = mutableSetOf<List<String>>()
        map.keys.forEach { c -> findSet(c, listOf(c)) { allSets.add(it.sorted()) } }

        check(1400 == allSets.filter { it.size == 3 }.filter { it.any { it.startsWith("t") } }.size)
        check("am,bc,cz,dc,gy,hk,li,qf,th,tj,wf,xk,xo" == allSets.maxByOrNull { it.size }!!.joinToString(","))
    }

    data class Computer(val id: String, val connections: MutableSet<String> = mutableSetOf())

}