package com.dehnes.adventofcode.v2020

import org.junit.jupiter.api.Test
import java.io.File

class Day21 {

    val foods = File("resources/2020/day21.txt").readLines().map { line ->
        val (ingredientsStr, allergensStr) = line.split(" (contains ", ")")
        ingredientsStr.split(" ") to allergensStr.split(", ")
    }

    @Test
    fun test() {
        val candidates = mutableMapOf<String, MutableList<String>>()
        val done = mutableSetOf<String>()
        foods.forEach { food ->
            food.first.forEach { ingr ->
                food.second.filter { !done.contains(it) }.forEach { allergen ->
                    if (foods.filter { it.second.contains(allergen) }.all { it.first.contains(ingr) }) {
                        candidates.getOrPut(allergen) { ArrayList() }.add(ingr)
                    }
                }
            }
            done.addAll(candidates.keys)
        }

        val result = mutableListOf<Pair<String, String>>()
        while (candidates.any { it.value.size == 1 }) {
            val allergen = candidates.keys.first { candidates[it]!!.size == 1 }
            val ingr = candidates.remove(allergen)!![0]
            result.add(allergen to ingr)
            candidates.forEach { it.value.remove(ingr) }
        }

        // 2380
        println("Part1: ${foods.map { it.first }.flatten().filter { ingr -> result.none { it.second == ingr } }.size}")
        // ktpbgdn,pnpfjb,ndfb,rdhljms,xzfj,bfgcms,fkcmf,hdqkqhh
        println("Part2: ${result.sortedBy { it.first }.joinToString(",") { it.second }}")
    }

}