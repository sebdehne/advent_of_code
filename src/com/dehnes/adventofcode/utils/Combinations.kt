package com.dehnes.adventofcode.utils

object Combinations {
    fun <T> combinations(list: List<T>): List<Pair<T, T>> {
        val result = mutableListOf<Pair<T, T>>()

        list.indices.forEach { l ->
            if (l == list.size - 1) return@forEach

            ((l + 1)..<(list.size)).forEach { r ->
                result.add(list[l] to list[r])
            }
        }

        return result
    }

    fun <T> getAllCombinations(
        depth: Int,
        possibilities: List<T>,
        current: List<T> = emptyList()
    ): List<List<T>> {
        if (depth == 1) {
            return possibilities.map {
                current + it
            }
        } else {
            return possibilities.flatMap {
                getAllCombinations(depth - 1, possibilities, current + it)
            }
        }
    }

}