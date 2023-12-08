package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day17 {

    val shapes = listOf(
        listOf(
            "####"
        ),
        listOf(
            ".#.",
            "###",
            ".#.",
        ),
        listOf(
            "..#",
            "..#",
            "###",
        ),
        listOf(
            "#",
            "#",
            "#",
            "#",
        ),
        listOf(
            "##",
            "##",
        ),
    ).map { shape ->
        val r = mutableListOf<Point>()
        shape.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') r.add(x to y)
            }
        }
        r.toList()
    }

    val emptyLine = ".......".toList().toCharArray()


    @Test
    fun run() {
        val jets = getLines().first().toCharArray()

        var removedLines = 0L
        val heights = IntArray(7) { 0 }
        var area = ArrayList<CharArray>()
        var firstRepeate: Pair<Pair<Int, Int>, String>? = null
        var firstRepeatRockCnt = 0L
        var firstRepeatHight = 0L
        var patternNumberOfRocks: Long? = null
        var patternHight: Long?
        var hightAdded = 0L

        var currentJet = 0

        val rockJetCombination = mutableMapOf<Pair<Int, Int>, MutableSet<String>>()

        //val rocksToFall = 2022
        val rocksToFall = 1_000_000_000_000
        var rockCnt = 0L
        while(rockCnt < rocksToFall) {
            val rockId = (rockCnt % shapes.size).toInt()
            val nextRock = shapes[rockId]
            val shapeHeight = nextRock.shapeHeight()


            // detect the repeating pattern
            if (hightAdded == 0L) {
                val combination = rockId to currentJet
                val cl = area.findCutoffLine(heights)
                if (cl != null) {
                    val areaString = area.joinToString { it.joinToString("") }
                    if (!rockJetCombination.getOrPut(combination) { mutableSetOf() }.add(areaString)) {
                        if (firstRepeate == null) {
                            firstRepeate = combination to areaString
                            firstRepeatRockCnt = rockCnt
                            firstRepeatHight = (heights.maxOf { it } + removedLines)
                        } else {
                            if (firstRepeate == combination to areaString) {
                                if (patternNumberOfRocks == null) {
                                    patternNumberOfRocks = rockCnt - firstRepeatRockCnt
                                    patternHight = (heights.maxOf { it } + removedLines) - firstRepeatHight

                                    val remainingRocks = rocksToFall - rockCnt
                                    val patternRepeats = remainingRocks / patternNumberOfRocks
                                    hightAdded = patternRepeats * patternHight
                                    rockCnt += patternRepeats * patternNumberOfRocks
                                }
                            }
                        }
                    }
                }
            }


            // make 3 lines room above + shape height
            val topLine = heights.maxOf { it }
            val room = area.size - topLine
            val needed = 3 + shapeHeight
            if (room < needed) {
                repeat(needed - room) {
                    area.add(emptyLine.clone())
                }
            }

            var rockPosition = 2 to (topLine + needed - 1)

            // move loop
            while (true) {
                //area.print(rockPosition to nextRock)

                // jet
                val nextJet = jets[currentJet++]
                if (currentJet == jets.size) {
                    currentJet = 0
                }

                val firstStep = (if (nextJet == '<') -1 else 1) to 0
                if (area.tryMove(nextRock, firstStep, rockPosition)) {
                    rockPosition += firstStep
                }
                //area.print(shapePos to nextRock)

                // down
                val secondStep = 0 to -1
                val canMove = area.tryMove(nextRock, secondStep, rockPosition)
                if (canMove) {
                    rockPosition += secondStep
                } else {
                    // landed
                    area.land(nextRock, rockPosition, heights)
                    //area.print()

                    val cutoffLine = area.findCutoffLine(heights)

                    if (cutoffLine != null) {
                        val cutLineY = cutoffLine.minOf { it.second }
                        removedLines += cutLineY
                        heights.indices.forEach { i ->
                            heights[i] -= cutLineY
                        }
                        area = area.drop(cutLineY) as ArrayList
                    }

                    break
                }
            }

            rockCnt++
        }

        println("rocksToFall=$rocksToFall hight=" + (heights.maxOf { it } + removedLines + hightAdded))
    }

    fun ArrayList<CharArray>.tryMove(nextRock: List<Point>, step: Point, rockPosition: Point): Boolean {
        val notOK = nextRock.firstOrNull { rp ->
            val rPos = ((rp.first + rockPosition.first) to (rockPosition.second - rp.second)) + step
            val yOK = rPos.second >= 0
            val xOK = rPos.first in 0..6
            !(yOK && xOK && this[rPos.second][rPos.first] == '.')
        }

        return notOK == null
    }

    fun ArrayList<CharArray>.findCutoffLine(heights: IntArray): List<Point>? {
        if (heights.any { it == 0 }) return null
        val positionWithDirection = mutableListOf<Pair<Point, Point>>()
        val startPos = 0 to (heights[0] - 1)
        var pos = startPos
        val steps = listOf(
            0 to 1,
            1 to 0,
            0 to -1,
            -1 to 0,
        )
        while (pos.first < (heights.size - 1)) {
            val findNextStep2 = { canGoBack: Boolean ->
                { s: Pair<Int, Int> ->
                    val candidate = pos + s
                    val backward = (s.first * -1) to (s.second * -1)
                    if (candidate.second < 0 || candidate.first < 0) {
                        false
                    } else if (!canGoBack && (pos to backward) == positionWithDirection.lastOrNull()) {
                        false
                    } else if ((candidate to s) in positionWithDirection) {
                        false
                    } else {
                        this[candidate.second][candidate.first] == '#'
                    }
                }
            }
            val next = steps.firstOrNull(findNextStep2(false))
                ?: steps.firstOrNull(findNextStep2(true))

            if (next != null) {
                pos += next
                positionWithDirection.add(pos to next)
            } else {
                return null
            }
        }

        return (listOf(startPos) + positionWithDirection.map { it.first }).distinct()
    }

    fun ArrayList<CharArray>.land(shape: List<Point>, shapePos: Point, heights: IntArray) {
        shape.forEach { rp ->
            val rPos = ((rp.first + shapePos.first) to (shapePos.second - rp.second))
            this[rPos.second][rPos.first] = '#'

            if (rPos.second >= heights[rPos.first]) {
                heights[rPos.first] = (rPos.second + 1)
            }
        }
    }

    fun ArrayList<CharArray>.print(shape: Pair<Point, List<Point>>? = null) {
        this.indices.reversed().forEach { y ->
            val line = this[y].clone()
            if (shape != null) {
                val (pos, sh) = shape

                sh.filter { pos.second - it.second == y }.forEach {
                    line[it.first + pos.first] = '@'
                }
            }

            println(line.joinToString(""))
        }
        println("-------")
        println("")
    }

    fun List<Point>.shapeHeight(): Int {
        val lowest = this.minOf { it.second }
        val highest = this.maxOf { it.second }
        return (highest - lowest) + 1
    }

}