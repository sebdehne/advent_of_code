package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getText
import com.dehnes.adventofcode.utils.plus
import org.junit.jupiter.api.Test
import java.util.*

class Day15 {

    val intcode = getText().split(",").map { it.toLong() }.toMutableList()

    data class Location(
        val pos: Pair<Int, Int>,
        val enterDirection: Int,
        val possibleWays: List<Int>,
    )

    @Test
    fun run() {
        val gridSize = 50
        val grid = Array(gridSize) { CharArray(gridSize) { ' ' } }

        var stepCounter = 0

        val toBeDiscovered = LinkedList<Int>()
        val pathStack = LinkedList<Location>()

        toBeDiscovered.offer(0)
        toBeDiscovered.offer(1)
        toBeDiscovered.offer(2)
        toBeDiscovered.offer(3)
        var currentlyDiscovering: Int? = null
        pathStack.addFirst(Location((gridSize / 2) to (gridSize / 2), -1, emptyList()))
        grid[pathStack.peek().pos.first][pathStack.peek().pos.second] = '.'

        val modifyCurrentLocation = { fn: (Location) -> Location ->
            val l = pathStack.removeFirst()
            pathStack.addFirst(fn(l))
        }

        val goBack = {
            val l = pathStack.removeFirst()
            if (pathStack.size == 1) {
                error("DONE") // we are back at start and have thus visited everything
            }
            modifyCurrentLocation {
                it.copy(possibleWays = it.possibleWays.filterNot { it == l.enterDirection })
            }
            l.enterDirection.turnAround()
        }

        val inputSource = {
            val nextDirection = if (currentlyDiscovering != null) {
                val r = currentlyDiscovering
                currentlyDiscovering = null
                r!!.turnAround()
            } else {
                if (toBeDiscovered.isEmpty()) {
                    val location = pathStack.peek()

                    // if there is only one way, then it is a dead-end, move back
                    if (location.possibleWays.isEmpty()) {
                        goBack()
                    } else {
                        val next = location.possibleWays.first()
                        val newPos = newPos(location.pos, next)
                        pathStack.addFirst(Location(newPos, next, emptyList()))
                        (0..3).filterNot { it == next.turnAround() }.forEach {
                            toBeDiscovered.offer(it)
                        }
                        next
                    }
                } else {
                    currentlyDiscovering = toBeDiscovered.pop()
                    currentlyDiscovering!!
                }
            }.toLong() + 1
            nextDirection
        }

        var stepsToOxygentTank: Int? = null
        var posOxygentTank: Pair<Int, Int>? = null

        val handleResult = { result: Long ->
            stepCounter++

            if (currentlyDiscovering != null) {
                val newPos = newPos(pathStack.peek().pos, currentlyDiscovering!!)
                when (result) {
                    0L -> {
                        grid[newPos.first][newPos.second] = '#'
                        currentlyDiscovering = null
                    }
                    1L -> {
                        grid[newPos.first][newPos.second] = '.'
                        modifyCurrentLocation {
                            it.copy(possibleWays = it.possibleWays + currentlyDiscovering!!)
                        }
                    }
                    else -> {
                        grid[newPos.first][newPos.second] = 'O'
                        stepsToOxygentTank = pathStack.size
                        posOxygentTank = newPos
                    }
                }

            } else {
                check(result == 1L)
            }
        }

        try {
            intcodeComputer(intcode, inputSource, handleResult)
        } catch (e: Exception) {
            if (!e.localizedMessage!!.contentEquals("DONE")) throw e
        }

        // part1
        check(stepsToOxygentTank == 374)

        var minutes: Int = -1
        val edges = LinkedList<Pair<Int, Int>>()
        edges.offer(posOxygentTank!!)

        while(edges.isNotEmpty()) {
            val currentEdges = edges.toList()
            edges.clear()

            currentEdges.forEach { e ->
                grid[e.first][e.second] = 'O'
                grid.openLocations(e).forEach {
                    edges.offer(it)
                }
            }
            minutes++
        }

        // part2
        check(minutes == 482)
    }

    private fun Array<CharArray>.openLocations(pos: Pair<Int, Int>): List<Pair<Int, Int>> = (0..3)
        .map { direction -> newPos(pos, direction) }
        .filter { p -> this[p.first][p.second] == '.' }


    private fun Int.turnAround() = when (this) {
        0 -> 1
        1 -> 0
        2 -> 3
        3 -> 2
        else -> error("")
    }

    private fun newPos(pos: Pair<Int, Int>, direction: Int) = when (direction) {
        0 -> pos + (-1 to 0)
        1 -> pos + (1 to 0)
        2 -> pos + (0 to -1)
        3 -> pos + (0 to 1)
        else -> error("")
    }

    private fun Array<CharArray>.printMap(pos: Pair<Int, Int>) {
        this.forEachIndexed { y, line ->
            val s = line.mapIndexed { x: Int, c: Char ->
                if (y == pos.first && x == pos.second) 'D' else c
            }.joinToString(separator = "")
            println(s)
        }
        println()
    }


    private fun Array<CharArray>.printMap() {
        this.forEach { line ->
            println(line.joinToString(separator = "") { "$it" })
        }
        println()
    }

}