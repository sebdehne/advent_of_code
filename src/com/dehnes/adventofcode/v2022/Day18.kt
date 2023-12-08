package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day18 {

    val points = getLines().map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Point3D(XYZ(x, y, z))
    }

    val limit = Point3D(
        XYZ(
            points.maxOf { it.location.x },
            points.maxOf { it.location.y },
            points.maxOf { it.location.z },
        ),
    )

    @Test
    fun part1() {
        var surfaces = points.size * 6L

        fun moveAlongAxis(
            selector1: (Point3D) -> Int,
            selector2: (Point3D) -> Int,
            mainSelector: (Point3D) -> Int,
        ) {
            points.map(selector1).distinct().forEach { a ->
                points.map(selector2).distinct().forEach { b ->
                    points.filter { selector1(it) == a && selector2(it) == b }.sortedBy(mainSelector).windowed(2, 1)
                        .forEach { (l, r) ->
                            if (mainSelector(r) - mainSelector(l) == 1) {
                                surfaces -= 2
                            }
                        }
                }
            }
        }

        moveAlongAxis({ it.location.z }, { it.location.y }, { it.location.x })
        moveAlongAxis({ it.location.x }, { it.location.z }, { it.location.y })
        moveAlongAxis({ it.location.y }, { it.location.x }, { it.location.z })

        expectThat(surfaces) isEqualTo 4504
    }

    @Test
    fun part2() {
        val origin = Point3D(XYZ(-1, -1, -1))

        val hasUnexploredSides = mutableMapOf<XYZ, Point3D>()
        hasUnexploredSides[origin.location] = origin
        val emptySpaces = mutableSetOf<XYZ>()
        emptySpaces.add(origin.location)

        var outsideSurfaces = 0

        while (hasUnexploredSides.isNotEmpty()) {
            val emptySpace = hasUnexploredSides.entries.first().value
            val side = emptySpace.exporedSides.entries.first { !it.value }.key
            emptySpace.exporedSides[side] = true
            if (emptySpace.exporedSides.all { it.value }) {
                hasUnexploredSides.remove(emptySpace.location)
            }

            val candidateLocation = emptySpace.location + side

            val xOK = candidateLocation.x in -1..(limit.location.x + 1)
            val yOK = candidateLocation.y in -1..(limit.location.y + 1)
            val zOK = candidateLocation.z in -1..(limit.location.z + 1)
            if (!xOK || !yOK || !zOK) continue

            if (emptySpaces.any { it == candidateLocation }) {
                // ignore
            } else if (points.any { it.location == candidateLocation }) {
                outsideSurfaces++
            } else {
                val newEmptySpace = Point3D(candidateLocation).apply {
                    exporedSides[XYZ(
                        side.x * -1,
                        side.y * -1,
                        side.z * -1,
                    )] = true
                }

                hasUnexploredSides[newEmptySpace.location] = newEmptySpace
                emptySpaces.add(newEmptySpace.location)
            }
        }

        expectThat(outsideSurfaces) isEqualTo 2556
    }

    companion object {
        data class Point3D(
            val location: XYZ,
            val exporedSides: MutableMap<XYZ, Boolean> = mutableMapOf(
                XYZ(1, 0, 0) to false,
                XYZ(-1, 0, 0) to false,
                XYZ(0, 1, 0) to false,
                XYZ(0, -1, 0) to false,
                XYZ(0, 0, 1) to false,
                XYZ(0, 0, -1) to false,
            )
        )

        data class XYZ(
            val x: Int,
            val y: Int,
            val z: Int,
        )

        operator fun XYZ.plus(other: XYZ) = copy(
            x = x + other.x,
            y = y + other.y,
            z = z + other.z,
        )


    }
}

