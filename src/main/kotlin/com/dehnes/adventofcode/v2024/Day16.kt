package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Maps.forEachPos
import com.dehnes.adventofcode.utils.Maps.get
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import java.util.*

class Day16 {

    @Test
    fun run() {
        val map = getLines().map { it.trim().toList().toTypedArray() }.toTypedArray()

        var start = PointInt(0, 0)
        var end = PointInt(0, 0)
        map.forEachPos { pos, value ->
            if (value == 'S') {
                start = pos
                map[pos.y][pos.x] = '.'
            } else if (value == 'E') {
                end = pos
                map[pos.y][pos.x] = '.'
            }
        }

        val startStep = Step(start, Direction.right, 0)
        val todo = LinkedList<List<Step>>()
        todo.add(listOf(startStep))
        val visited = mutableMapOf<Pair<PointInt, Direction>, Step>()
        visited[start to Direction.right] = startStep
        val completed = LinkedList<List<Step>>()

        while (todo.isNotEmpty()) {
            val path = todo.remove()
            val step = path.last()

            listOf(0, 1, 3).forEach { rotation ->
                val direction = step.dir.rotate90Degrees(rotation)
                val nextPos = step.pos + direction
                if (map.get(nextPos) != '.') return@forEach

                val nextStep = Step(nextPos, direction, step.score + if (rotation == 0) 1 else 1001)

                visited[nextStep.pos to nextStep.dir]?.let {
                    if (it.score < nextStep.score) {
                        return@forEach // the other is cheaper
                    }
                }

                visited[nextStep.pos to nextStep.dir] = nextStep
                if (nextStep.pos == end) {
                    completed.add(path + nextStep)
                } else {
                    todo.add(path + nextStep)
                }
            }
        }

        val bestScore = completed.minByOrNull { it.last().score }!!.last().score
        val bestPaths = completed.filter { it.last().score == bestScore }

        check(122492L == bestScore)
        check(520 == bestPaths.flatMap { it.map { it.pos } }.distinct().size)
    }


    data class Step(val pos: PointInt, val dir: Direction, var score: Long)
}