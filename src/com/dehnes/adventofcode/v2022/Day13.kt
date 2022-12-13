package com.dehnes.adventofcode.v2022

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.*

class Day13 {

    @Test
    fun run() {
        val pairs = inputLines(13).filterNot { it.isBlank() }.chunked(2).map {
            mapElements(parse(it[0])) to mapElements(parse(it[1]))
        }

        val indicesOfRightOrder = pairs.mapIndexedNotNull { index: Int, pair: Pair<List<Any>, List<Any>> ->
            if (isInRightOrder(pair.first, pair.second) == true) index + 1 else null
        }

        val div1 = arrayListOf(arrayListOf(arrayListOf(2)))
        val div2 = arrayListOf(arrayListOf(arrayListOf(6)))
        val allPackets = pairs.flatMap { listOf(it.first, it.second) } + div1 + div2
        val sorted = allPackets.sortedWith { left, right ->
            when (isInRightOrder(left, right)) {
                true -> -1
                false -> 1
                else -> 0
            }
        }

        val first = sorted.indexOfFirst { it.toString() == div1[0].toString() } + 1
        val second = sorted.indexOfFirst { it.toString() == div2[0].toString() } + 1

        expectThat(indicesOfRightOrder.sum()) isEqualTo 5659
        expectThat(first * second) isEqualTo 22110
    }

    fun isInRightOrder(left: List<Any>, right: List<Any>): Boolean? {
        var pos = 0
        while (true) {
            val l = left.getOrNull(pos)
            val r = right.getOrNull(pos)
            when {
                l is Int && r is Int -> if (l < r) {
                    return true
                } else if (l > r) {
                    return false
                }

                l == null && r != null -> return true
                l != null && r == null -> return false
                l is List<*> && r is List<*> -> isInRightOrder(l as List<Any>, r as List<Any>)?.let {
                    return it
                }

                l is Int && r is List<*> -> isInRightOrder(listOf(l), r as List<Any>)?.let {
                    return it
                }

                l is List<*> && r is Int -> isInRightOrder(l as List<Any>, listOf(r))?.let {
                    return it
                }

                l == null && r == null -> return null
            }

            pos++
        }
    }

    fun mapElements(elements: List<Element>): List<Any> {
        var remaining = LinkedList(elements)
        val result = mutableListOf<Any>()
        while (remaining.isNotEmpty()) {
            when (val next = remaining.removeFirst()) {
                is ListStart -> {
                    val endPos = findEndPos(remaining)
                    val subList = remaining.subList(0, endPos)
                    remaining = LinkedList(remaining.subList(endPos + 1, remaining.size))
                    result.add(mapElements(subList))
                }

                is IntWrapper -> result.add(next.int)
                else -> error("")
            }
        }
        return result
    }

    fun findEndPos(elements: List<Element>): Int {
        var currentLevel = 1
        var pos = 0
        for (el in elements) {
            when (el) {
                is ListStart -> currentLevel++
                is ListEnd -> currentLevel--
                else -> {}
            }
            if (currentLevel == 0) {
                break
            }
            pos++
        }
        return pos
    }

    fun parse(line: String): List<Element> {
        val currentBuffer = mutableListOf<Char>()
        val parts: MutableList<Element> = mutableListOf()

        val flushBuffer = {
            if (currentBuffer.isNotEmpty()) {
                parts.addAll(
                    currentBuffer
                        .joinToString(separator = "")
                        .split(",")
                        .filter { it.isNotBlank() }
                        .map { IntWrapper(it.toInt()) }
                )
                currentBuffer.clear()
            }
        }


        line.forEach { c ->
            when (c) {
                '[' -> {
                    flushBuffer()
                    parts.add(ListStart)
                }

                ']' -> {
                    flushBuffer()
                    parts.add(ListEnd)
                }

                else -> currentBuffer.add(c)
            }
        }
        flushBuffer()

        return parts
    }


}

sealed class Element
data class IntWrapper(
    val int: Int
) : Element()

object ListStart : Element()
object ListEnd : Element()

