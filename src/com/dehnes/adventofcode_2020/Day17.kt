package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day17 {

    val initial2 = File("resources/day17.txt").readLines()
            .map { it.toList().map { it == '#' } }

    @Test
    fun test() {
        println(calc(listOf(initial2), 6)) // 209
        println(calc(listOf(listOf(initial2)), 6)) // 1492
    }

    fun calc(list: List<*>, rounds: Int): Int {
        var previousResult = grow(list)
        var current = zero(previousResult)
        for (cycle in 1..rounds) {
            forEachCoordinate(previousResult) { coordinates, cube ->
                val active = activeNeighbors(previousResult, coordinates)
                val newMe = if (cube) {
                    active == 2 || active == 3
                } else {
                    active == 3
                }
                set(current, newMe, coordinates)
            }

            previousResult = grow(current)
            current = zero(previousResult)
        }
        return countActive(previousResult)
    }

    fun forEachCoordinate(list: List<*>, currents: List<Int> = emptyList(), fn: (List<Int>, b: Boolean) -> Unit) {
        if (list.isNotEmpty()) {
            when (list[0]) {
                is Boolean -> {
                    list.forEachIndexed { index, cube -> fn(currents + index, cube as Boolean) }
                }
                else -> {
                    list.forEachIndexed { index, subList ->
                        forEachCoordinate(subList as List<*>, currents + index, fn)
                    }
                }
            }
        }
    }

    fun set(list: MutableList<*>, b: Boolean, coordinates: List<Int>) {
        if (coordinates.size == 1) {
            (list as MutableList<Any>)[coordinates.first()] = b
        } else {
            set(list[coordinates.first()] as MutableList<*>, b, coordinates.slice(1 until coordinates.size))
        }
    }

    fun activeNeighbors(list: List<*>, coordinates: List<Int>, currents: List<Int> = emptyList()): Int =
            if (currents.size == coordinates.size) {
                if (currents == coordinates.toList()) {
                    0
                } else {
                    if (at(list, currents)) 1 else 0
                }
            } else {
                val current = coordinates[currents.size]
                ((current - 1)..(current + 1)).map { i ->
                    activeNeighbors(
                            list,
                            coordinates,
                            currents = currents + i
                    )
                }.sum()
            }

    fun grow(list: List<*>): MutableList<*> {
        return if (list.isEmpty()) {
            mutableListOf(false)
        } else {
            val probe = list[0]
            if (probe is List<*>) {
                val result = mutableListOf<Any>()
                result.add(grow(zero(probe)))
                result.addAll(list.map { grow(it as List<*>) })
                result.add(grow(zero(probe)))
                result
            } else {
                (listOf(false) + list + false).toMutableList()
            }
        }
    }

    fun at(list: List<*>, coordinates: List<Int>): Boolean = if (coordinates.size == 1) {
        (list as List<Boolean>).getOrElse(coordinates.first()) { false }
    } else {
        at(list.getOrElse(coordinates.first()) { emptyList<Any>() } as List<*>, coordinates.slice(1 until coordinates.size))
    }

    fun zero(list: List<*>): MutableList<*> = if (list.isEmpty()) {
        mutableListOf<Any>()
    } else {
        when (list[0]) {
            is List<*> -> list.map { zero(it as List<*>) }.toMutableList()
            is Boolean -> list.map { false }.toMutableList()
            else -> error("")
        }
    }


    fun countActive(list: List<*>): Int = if (list.isEmpty()) {
        0
    } else {
        val probe = list[0]
        if (probe is Boolean) {
            list.sumBy { if (it as Boolean) 1 else 0 }
        } else {
            list.sumBy { countActive(it as List<*>) }
        }
    }

}
