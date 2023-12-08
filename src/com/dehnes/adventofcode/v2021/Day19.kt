package com.dehnes.adventofcode.v2021

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day19 {

    data class PointInSpace(
        val x: Int,
        val y: Int,
        val z: Int
    ) {
        companion object {
            fun parse(str: String): PointInSpace {
                val p = str.split(",")
                return PointInSpace(
                    p[0].toInt(),
                    p[1].toInt(),
                    p[2].toInt(),
                )
            }
        }

        infix operator fun plus(other: PointInSpace) = PointInSpace(
            other.x + x,
            other.y + y,
            other.z + z,
        )

        infix operator fun minus(other: PointInSpace) = PointInSpace(
            other.x - x,
            other.y - y,
            other.z - z,
        )

        override fun toString() = "$x,$y,$z"

        private fun rotateX() = PointInSpace(this.x, this.z * -1, this.y)
        private fun rotateY() = PointInSpace(this.z * -1, this.y, this.x)
        private fun rotateZ() = PointInSpace(this.y * -1, this.x, this.z)

        fun rotate(n: Int): PointInSpace {
            return when (n) {
                in 0..3 -> this to n.mod(4)
                in 4..7 -> this.rotateY() to n.mod(4)
                in 8..11 -> this.rotateY().rotateY() to n.mod(4)
                in 12..15 -> this.rotateY().rotateY().rotateY() to n.mod(4)
                in 16..19 -> this.rotateZ() to n.mod(4)
                in 20..23 -> this.rotateZ().rotateZ().rotateZ() to n.mod(4)
                else -> error("")
            }.let { (b, t) ->
                (0 until t).fold(b) { acc, i ->
                    acc.rotateX()
                }
            }
        }
    }

    data class Scanner(
        val id: Int,
        val beacons: List<PointInSpace>,
    ) {

        fun rotate(orientation: Int) = Scanner(
            id,
            beacons.map { it.rotate(orientation) }
        )

        fun getSameDelta(delta: PointInSpace): Pair<Int, Int>? {
            beacons.forEachIndexed { leftIndex, leftBeacon ->
                beacons.forEachIndexed { rightIndex, rightBeacon ->
                    if (leftIndex != rightIndex) {
                        if (leftBeacon - rightBeacon == delta) {
                            return leftIndex to rightIndex
                        }
                    }
                }
            }
            return null
        }

    }

    private fun findMatches(left: Scanner, right: Scanner): Map<Int, Int> {
        val m = mutableMapOf<Int, Int>()
        val candidates = mutableMapOf<Int, List<Int>>()

        left.beacons.forEachIndexed { leftIndex, leftBeacon ->
            left.beacons.forEachIndexed { rightIndex, rightBeacon ->
                if (leftIndex != rightIndex) {
                    val delta = leftBeacon - rightBeacon

                    val matchingPair = right.getSameDelta(delta)?.toList()

                    if (matchingPair != null) {
                        if (leftIndex !in m) {
                            if (leftIndex in candidates) {
                                val intersect = candidates[leftIndex]!!.intersect(matchingPair)
                                if (intersect.size == 1) {
                                    val i = intersect.single()
                                    m[leftIndex] = i
                                    candidates.remove(leftIndex)
                                }
                            } else {
                                candidates[leftIndex] = matchingPair
                            }
                        }
                        if (rightIndex !in m) {
                            if (rightIndex in candidates) {
                                val intersect = candidates[rightIndex]!!.intersect(matchingPair)
                                if (intersect.size == 1) {
                                    val i = intersect.single()
                                    m[rightIndex] = i
                                    candidates.remove(rightIndex)
                                }
                            } else {
                                candidates[rightIndex] = matchingPair
                            }
                        }
                    }
                }
            }
        }

        return m
    }

    @Test
    fun run() {
        val scanners = readData()

        val totalBeacons = mutableSetOf<PointInSpace>()
        totalBeacons.addAll(scanners[0].beacons)
        val relatives = mutableMapOf<Int, PointInSpace>()
        relatives[0] = PointInSpace(0, 0, 0)
        val orientations = mutableMapOf<Int, Int>()
        orientations[0] = 0
        val tested = mutableSetOf<Pair<Int, Int>>()

        while (true) {
            var foundMatch = false

            run outer@{
                scanners.forEachIndexed { leftIndex, leftScanner ->
                    scanners.forEachIndexed { rightIndex, rightScanner ->
                        if (leftIndex != rightIndex && (leftIndex to rightIndex) !in tested && leftIndex in relatives) {

                            val leftDelta = relatives[leftIndex]!!
                            val leftScannerAdjusted = Scanner(
                                leftScanner.id,
                                leftScanner.rotate(orientations[leftIndex]!!).beacons.map { it + leftDelta }
                            )

                            tested.add(leftIndex to rightIndex)
                            tested.add(rightIndex to leftIndex)

                            for (tryOrientation in 0..23) {
                                val rightScannerRotated = rightScanner.rotate(tryOrientation)
                                val matches = findMatches(leftScannerAdjusted, rightScannerRotated)
                                if (matches.size >= 12) {
                                    val firstPair = matches.toList().first()
                                    val l = leftScannerAdjusted.beacons[firstPair.first]
                                    val r = rightScannerRotated.beacons[firstPair.second]
                                    val deltaToZero = r - l
                                    orientations[rightIndex] = tryOrientation
                                    relatives[rightIndex] = deltaToZero
                                    totalBeacons.addAll(rightScannerRotated.beacons.map { it + deltaToZero })

                                    println(leftIndex to rightIndex)
                                    foundMatch = true
                                    return@outer
                                }
                            }
                        }
                    }
                }
            }


            if (!foundMatch) {
                break
            }
        }

        var maxDistance = 0
        relatives.toList().forEach { l ->
            relatives.toList().forEach { r ->
                val d =
                    (l.second.x - r.second.x).absoluteValue + (l.second.y - r.second.y).absoluteValue + (l.second.z - r.second.z).absoluteValue
                if (d > maxDistance) {
                    maxDistance = d
                }
            }
        }


        check(totalBeacons.size == 408)
        check(maxDistance == 13348)
    }

    private fun readData(): List<Scanner> {
        val result = mutableListOf<Scanner>()
        var currentScanner = -1
        val currentBeacons = mutableListOf<PointInSpace>()
        getLines().forEach { line ->
            if (line.startsWith("--- scanner ")) {
                if (currentBeacons.isNotEmpty()) {
                    result.add(
                        Scanner(
                            currentScanner,
                            currentBeacons.toList()
                        )
                    )
                }
                currentScanner = line.split(" ")[2].toInt()
                currentBeacons.clear()
            } else if (line.trim().isNotBlank()) {
                currentBeacons.add(PointInSpace.parse(line.trim()))
            }
        }
        if (currentBeacons.isNotEmpty()) {
            result.add(
                Scanner(
                    currentScanner,
                    currentBeacons.toList(),
                )
            )
        }

        return result
    }

}
